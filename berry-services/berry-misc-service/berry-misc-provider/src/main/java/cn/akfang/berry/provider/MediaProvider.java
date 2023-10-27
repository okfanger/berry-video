package cn.akfang.berry.provider;

import cn.akfang.berry.common.model.dto.QiniuUploadDTO;
import cn.akfang.berry.common.model.response.FileUploadToken;
import cn.akfang.berry.manager.QiniuOSSManager;
import cn.akfang.berry.service.MediaService;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

@DubboService
@Slf4j
public class MediaProvider implements MediaService {
    @Autowired
    QiniuOSSManager qiniuOSSManager;

    @Override
    public FileUploadToken getUploadToken() {
        String uploadToken = qiniuOSSManager.getUploadToken();
        return FileUploadToken.builder()
                .upToken(uploadToken)
                .build();
    }

    @Override
    public String uploadFile(String upToken) {
        QiniuUploadDTO qiniuUploadDTO = qiniuOSSManager.uploadFile(upToken, new File("/Users/fang/test1.mov"));
        log.info("uploadFile: {}", JSONUtil.toJsonStr(qiniuUploadDTO));
        return "success";
    }


}
