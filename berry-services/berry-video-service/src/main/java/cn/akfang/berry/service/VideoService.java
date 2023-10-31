package cn.akfang.berry.service;

import cn.akfang.berry.common.model.entity.VideoPO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface VideoService extends IService<VideoPO> {

    Boolean isOwner(Long userId, Long videoId);

//    IPage<VideoVO> selectVideoVOPage(IPage<VideoVO> page, VideoVO params);
}
