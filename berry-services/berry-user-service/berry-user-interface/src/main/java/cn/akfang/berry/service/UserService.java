package cn.akfang.berry.service;


import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.common.model.request.UserLoginRequest;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<UserPO> {
    UserPO getUserByUsername(String username);

    UserTokenResponse login(UserLoginRequest userLoginRequest) throws BerryRpcException;

    UserTokenResponse smsAuthLogin();
}
