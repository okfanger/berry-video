package cn.akfang.berry.provider;

import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.mapper.VideoMapper;
import cn.akfang.berry.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class VideoProvider extends ServiceImpl<VideoMapper, VideoPO> implements VideoService {

}
