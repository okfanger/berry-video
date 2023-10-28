package cn.akfang.berry.manager;

import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.model.dto.QiniuUploadDTO;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
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

//    @Value("expireTime")
//    private Long expireTime;

    private Auth core;

    @Override
    public void afterPropertiesSet() {
        this.core = Auth.create(accessKey, secretKey);
    }

    public String getUploadToken() {
        StringMap putPolicy = new StringMap();
        putPolicy.put("callbackUrl", "http://berry-api.akfang.cn/misc/upload/callback");
        putPolicy.put("callbackBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        putPolicy.put("callbackBodyType", "application/json");
        String fileName = IdUtil.simpleUUID() + "_" + RandomUtil.randomString(4);
        //数据处理指令，支持多个指令
        String saveMp4Entry = String.format("%s:%s.mp4", bucket, fileName);
        String saveAsM3U8 = String.format("avthumb/m3u8/noDomain/1/vb/500k/t/", bucket, fileName);
        //将多个数据处理指令拼接起来
        String persistentOpfs = StringUtils.join(new String[]{
                saveMp4Entry, saveAsM3U8
        }, ";");
        putPolicy.put("persistentOps", persistentOpfs);
        //数据处理队列名称，必填
        putPolicy.put("persistentPipeline", "berry-pipeline1");
        //数据处理完成结果通知地址
        putPolicy.put("persistentNotifyUrl", "http://berry-api.akfang.cn/misc/upload/callback");

        long expireSeconds = 3600;
        String upToken = core.uploadToken(bucket, null, expireSeconds, putPolicy);
        return upToken;
    }

    public QiniuUploadDTO uploadFile(String upToken, File file) {
        Configuration cfg = new Configuration(Region.huabei());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        cfg.useHttpsDomains = false;
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response response = uploadManager.put(file, null, upToken);
            //解析上传成功的结果
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
