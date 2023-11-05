package cn.akfang.berry.service;

import cn.akfang.berry.common.model.dto.VideoActionDTO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.model.response.VideoVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface VideoService extends IService<VideoPO> {

    Boolean isOwner(Long userId, Long videoId);

    List<VideoPO> selectVideoPOByChannelId(Long channelId);

    List<VideoVO> buildVideoVO(List<VideoPO> sourceList, Map<Long, VideoActionDTO> actionInfo, Map<Long, UserBaseVO> authorInfo);

}
