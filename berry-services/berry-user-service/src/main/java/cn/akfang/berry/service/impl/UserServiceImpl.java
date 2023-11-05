package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.constants.GlobalConstants;
import cn.akfang.berry.common.constants.WxConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.enums.UserGenderEnum;
import cn.akfang.berry.common.enums.UserRoleEnum;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.common.model.request.UserInfoUpdateDTO;
import cn.akfang.berry.common.model.request.WxLoginRequest;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import cn.akfang.berry.common.model.response.UserVo;
import cn.akfang.berry.common.utils.BerryJWTUtil;
import cn.akfang.berry.mapper.UserMapper;
import cn.akfang.berry.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public String generateWxLoginClientId() {
        String nanoId = IdUtil.fastUUID();
        String code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set(WxConstants.KEY + ":" + nanoId, code.trim(), WxConstants.EXPIRE_TIME, TimeUnit.SECONDS);
        log.info("generateWxLoginClientId: {}", code);
        return nanoId;
    }

    @Override
    public UserTokenResponse wxLogin(WxLoginRequest request) {
        String openId = (String) redisTemplate.opsForValue().get(WxConstants.FROM_USER_OPENID2CLIENT_ID_KEY + ":" + request.getClientId());
        if (StrUtil.isBlank(openId)) {
            throw new BerryRpcException(ErrorCode.SYSTEM_ERROR, "openId为空");
        }
        Optional<UserPO> userPO = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(UserPO::getUserName, openId)
                .oneOpt();
        if (!userPO.isPresent()) {
            UserPO newUser = UserPO.builder()
                    .nickName(String.format("树莓拍用户%d", openId.hashCode()))
                    .userAvatar("default")
                    .userRole(UserRoleEnum.USER.getCode())
                    .userPassword("123456")
                    .gender(UserGenderEnum.MALE.getCode())
                    .userName(openId)
                    .build();
            this.save(newUser);
            userPO = Optional.of(newUser);
        }
        //

        String token = BerryJWTUtil.createToken(
                MapUtil.builder()
                        .put("id", userPO.get().getId())
                        .put("username", userPO.get().getUserName())
                        .build()
        );

        // 销毁现场
        redisTemplate.opsForValue().getOperations().delete(WxConstants.FROM_USER_OPENID2CLIENT_ID_KEY + ":" + request.getClientId());
        redisTemplate.opsForValue().getOperations().delete(WxConstants.KEY + ":" + request.getClientId());
        return UserTokenResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public UserPO getByUserId(Long userId) {
        return new LambdaQueryChainWrapper<>(baseMapper)
                .eq(UserPO::getId, userId)
                .oneOpt().orElse(null);
    }

    @Override
    public Boolean updateUserInfo(UserInfoUpdateDTO dto, Long userId) {

        LambdaUpdateWrapper<UserPO> qw = new LambdaUpdateWrapper<>();
        qw.eq(UserPO::getId, userId);
        if (StrUtil.isNotBlank(dto.getNickName())) {
            qw.set(UserPO::getNickName, dto.getNickName());
        }
        if (ObjectUtil.isNotNull(dto.getGender())) {
            qw.set(UserPO::getGender, ObjectUtil.equal(dto.getGender(), 1) ? 1 : 0);
        }
        return this.update(qw);
    }

    @Override
    public UserVo getUserVoById(Long userId) {
        UserPO byId = getById(userId);
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(byId, userVo);
        userVo.setUserAvatar(GlobalConstants.OSS_URL + "/" + byId.getUserAvatar());
        return userVo;
    }

    @Override
    public List<Long> listIdsMinutesAgo(Long minute) {
        LambdaQueryWrapper<UserPO> qw = new LambdaQueryWrapper<>();
        // 查找更新时间在2分钟前的视频
        qw.select(UserPO::getId)
                .geSql(UserPO::getUpdateTime, "DATE_SUB(NOW(), INTERVAL " + minute + " MINUTE)");
        return list(qw).stream().map(UserPO::getId).collect(Collectors.toList());

    }
}
