package cn.akfang.berry.service;

import cn.akfang.berry.common.model.dto.CommentAddDTO;
import cn.akfang.berry.common.model.entity.CommentPO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.CommentVo;
import cn.akfang.berry.common.model.response.UserBaseVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author fang
 * @description 针对表【t_comment(评论表)】的数据库操作Service
 * @createDate 2023-10-30 17:53:47
 */
public interface CommentService extends IService<CommentPO> {
    CommentVo buildCommonVO(CommentPO commentPO, UserBaseVO userBaseVO, Long userId);

    boolean addComment(VideoPO videoPO, Long userId, CommentAddDTO commentAddDTO);

    Wrapper<CommentPO> getFeedQueryWrapper(Long videoId, String orderBy);

    Integer getCommentCountByVideoId(Long videoId);

    Map<String, Integer> getCommentCountGroupByVideoIds();
}
