package cn.akfang.berry.controller;

import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.enums.UserGenderEnum;
import cn.akfang.berry.common.enums.UserRoleEnum;
import cn.akfang.berry.common.enums.WxConstants;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.UserService;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.common.model.request.WxLoginRequest;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import cn.akfang.berry.common.utils.BerryJWTUtil;
import cn.akfang.berry.mapper.UserMapper;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class UserProvider extends ServiceImpl<UserMapper, UserPO> implements UserService, IService<UserPO> {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public UserPO getUserByUsername(String username) throws BerryRpcException {
        Optional<UserPO> user = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(UserPO::getUserName, username)
                .select()
                .oneOpt();
        return user.orElse(null);
    }

    public String generateWxLoginClientId() throws BerryRpcException {
        String nanoId = IdUtil.fastUUID();
        String code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set(WxConstants.KEY + ":" + nanoId, code.trim(), WxConstants.EXPIRE_TIME, TimeUnit.SECONDS);
        log.info("generateWxLoginClientId: {}", code);
        return nanoId;
    }

    @Override
    public UserTokenResponse wxLogin(WxLoginRequest request) throws BerryRpcException {
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

}
