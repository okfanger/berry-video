package cn.akfang.berry.manager;

import cn.akfang.berry.common.constants.GlobalConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.model.dto.QiniuUploadDTO;
import cn.hutool.core.util.IdUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;


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

    @Value("${qiniu.oss.saveAs}")
    private String saveAsFop;

    @Value("${qiniu.oss.toM3U8}")
    private String m3u8Fop;

    @Value("${qiniu.oss.toCover}")
    private String coverFop;

    @Value("${qiniu.oss.expireSeconds}")
    private Long expireSeconds;

    private Auth core;


    @Override
    public void afterPropertiesSet() {
        this.core = Auth.create(accessKey, secretKey);
    }

    public String getVideoUploadToken() {
        StringMap putPolicy = new StringMap();
        putPolicy.put("callbackUrl", GlobalConstants.GATEWAY_URL + "/misc/oss/upload/callback?type=videoUpload");
        putPolicy.put("callbackBody", "{\"key\":\"" + "$(key)" + "\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        putPolicy.put("callbackBodyType", "application/json");
        putPolicy.put("saveKey", String.format("%s_%s", IdUtil.simpleUUID(), IdUtil.nanoId(6)));
        String saveAsName = String.format("%s:$(key)_saveAs.mp4", bucket);
        putPolicy.put("persistentOps", StringUtils.join(Arrays.asList(
                String.format(saveAsFop, UrlSafeBase64.encodeToString(saveAsName)),
                String.format(m3u8Fop, UrlSafeBase64.encodeToString(String.format("%s.m3u8", saveAsName.replace(".mp4", "")))),
                String.format(coverFop, UrlSafeBase64.encodeToString(String.format("%s_cover.jpg", saveAsName.replace(".mp4", ""))))
        ), ";"));
        putPolicy.put("persistentPipeline", pipeline);
        putPolicy.put("persistentNotifyUrl", GlobalConstants.GATEWAY_URL + "/misc/oss/transform/callback");
        return core.uploadToken(bucket, null, expireSeconds, putPolicy);
    }

    public String getAvatarUploadToken(Long userId) {
        StringMap putPolicy = new StringMap();
        String avatarKey = "user_" + userId + ".avatar";
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        putPolicy.put("saveKey", avatarKey);
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

    public QiniuUploadDTO uploadFileByInputStream(String upToken, InputStream inputStream) {
        Configuration cfg = new Configuration(Region.huabei());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        cfg.useHttpsDomains = false;
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response response = uploadManager.put(inputStream, null, upToken, null, null);
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

    public boolean isValidCallback(String originHeader, Auth.Request request) {
        return core.isValidCallback(originHeader, request);
    }


    public void batchRemoveByKeys(String[] keyList) {
        try {
            Configuration cfg = new Configuration(Region.huabei());
            cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
            cfg.useHttpsDomains = false;
            BucketManager bucketManager = new BucketManager(core, cfg);
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keyList.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keyList[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    log.info("delete success: key={}", key);
                } else {
                    log.error("delete error: key={}, err={}", key, status.data.error);
                }
            }
        } catch (QiniuException ex) {
            throw new BerryRpcException(ErrorCode.QINIU_DELETE_ERROR, ex.getMessage());
        }
    }
}
