package cn.akfang.berry.provider;

import cn.akfang.berry.common.model.response.FileUploadToken;
import cn.akfang.berry.manager.QiniuOSSManager;
import cn.akfang.berry.service.MediaService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
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
}
