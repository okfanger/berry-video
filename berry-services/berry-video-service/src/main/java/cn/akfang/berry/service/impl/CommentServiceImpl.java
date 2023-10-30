package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.model.entity.CommentPO;
import cn.akfang.berry.mapper.CommentMapper;
import cn.akfang.berry.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author fang
 * @description 针对表【t_comment(评论表)】的数据库操作Service实现
 * @createDate 2023-10-30 17:53:47
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentPO>
        implements CommentService {

}




