package cn.akfang.berry.service;

import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.common.model.request.UserInfoUpdateDTO;
import cn.akfang.berry.common.model.request.WxLoginRequest;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import cn.akfang.berry.common.model.response.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<UserPO> {

    String generateWxLoginClientId();

    UserTokenResponse wxLogin(WxLoginRequest request);

    UserPO getByUserId(Long userId);

    Boolean updateUserInfo(UserInfoUpdateDTO dto, Long userId);

    UserVo getUserVoById(Long userId);
}
