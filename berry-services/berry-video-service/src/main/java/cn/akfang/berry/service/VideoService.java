package cn.akfang.berry.service;

import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.model.response.VideoVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface VideoService extends IService<VideoPO> {

    Boolean isOwner(Long userId, Long videoId);

    VideoVO buildVideoVO(VideoPO item, UserBaseVO userBaseVO, Long currentUserId);

    Page<VideoVO> selectVideoVOPageByAuthorId(Long fromUserId, UserBaseVO toUser, Page<VideoVO> page);
}
