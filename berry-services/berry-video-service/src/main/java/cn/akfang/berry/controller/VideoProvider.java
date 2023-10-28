package cn.akfang.berry.controller;

import cn.akfang.berry.common.feign.client.VideoService;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.mapper.VideoMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class VideoProvider extends ServiceImpl<VideoMapper, VideoPO> implements VideoService, IService<VideoPO> {

}
