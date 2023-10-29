package cn.akfang.berry.manager;

import cn.akfang.berry.common.constants.GlobalConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.model.dto.QiniuUploadDTO;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;


@Slf4j
@Component
public class QiniuOSSManager implements InitializingBean {

    @Value("${qiniu.oss.accessKey}")
    private String accessKey;

    @Value("${qiniu.oss.secretKey}")
    private String secretKey;

    @Value("${qiniu.oss.bucket}")
    private String bucket;

    @Value("${qiniu.oss.pipeline}")
    private String pipeline;

    @Value("${qiniu.oss.persistentOps}")
    String[] persistentOps;

    @Value("${qiniu.oss.expireSeconds}")
    private Long expireSeconds;

    private Auth core;

    @Override
    public void afterPropertiesSet() {
        this.core = Auth.create(accessKey, secretKey);
    }

    public String getUploadToken() {
        StringMap putPolicy = new StringMap();
        putPolicy.put("callbackUrl", GlobalConstants.GATEWAY_URL + "/misc/oss/upload/callback");
        putPolicy.put("callbackBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        putPolicy.put("callbackBodyType", "application/json");
        putPolicy.put("persistentOps", StringUtils.join(persistentOps, ";"));
        putPolicy.put("persistentPipeline", pipeline);
        putPolicy.put("persistentNotifyUrl", GlobalConstants.GATEWAY_URL + "/misc/oss/transform/callback");
        return core.uploadToken(bucket, null, expireSeconds, putPolicy);
    }

    public QiniuUploadDTO uploadFile(String upToken, File file) {
        Configuration cfg = new Configuration(Region.huabei());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        cfg.useHttpsDomains = false;
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response response = uploadManager.put(file, null, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return QiniuUploadDTO.builder()
                    .key(putRet.key)
                    .hash(putRet.hash)
                    .build();
        } catch (QiniuException ex) {
            log.error("uploadFile: {}", String.valueOf(ex.response));
            throw new BerryRpcException(ErrorCode.QINIU_UPLOAD_ERROR, ex.getMessage());
        }
    }
}
