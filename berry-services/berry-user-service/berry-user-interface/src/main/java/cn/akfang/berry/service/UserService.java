package cn.akfang.berry.service;


import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.model.entity.User;
import cn.akfang.berry.common.model.request.UserLoginRequest;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    User getUserByUsername(String username);

    UserTokenResponse login(UserLoginRequest userLoginRequest) throws BerryRpcException;
}
