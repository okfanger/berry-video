package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.mapper.VideoMapper;
import cn.akfang.berry.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, VideoPO> implements VideoService {
}
