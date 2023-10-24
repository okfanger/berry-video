package cn.akfang.berry.provider;

import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.common.model.request.UserLoginRequest;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import cn.akfang.berry.common.utils.BerryJWTUtil;
import cn.akfang.berry.mapper.UserMapper;
import cn.akfang.berry.service.UserService;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Optional;

@DubboService
public class UserProvider extends ServiceImpl<UserMapper, UserPO> implements UserService {
    @Override
    public UserPO getUserByUsername(String username) {
        Optional<UserPO> user = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(UserPO::getUserName, username)
                .select()
                .oneOpt();
        return user.orElse(null);
    }

    @Override
    public UserTokenResponse login(UserLoginRequest userLoginRequest) {
        Optional<UserPO> userByUsername = Optional.ofNullable(getUserByUsername(userLoginRequest.getUsername()));
        if (userByUsername.isPresent() && ObjectUtil.equal(userByUsername.get().getUserPassword(), userLoginRequest.getPassword())) {
            String token = BerryJWTUtil.createToken(
                    MapUtil.builder()
                            .put("id", userByUsername.get().getId())
                            .put("username", userByUsername.get().getUserName())
                            .build()
            );
            return UserTokenResponse.builder()
                    .token(token)
                    .build();
        } else {
            throw new BerryRpcException(ErrorCode.WRONG_USERNAME_OR_PASSWORD);
        }
    }
}
