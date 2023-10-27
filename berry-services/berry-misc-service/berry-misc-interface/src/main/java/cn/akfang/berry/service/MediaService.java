package cn.akfang.berry.service;


import cn.akfang.berry.common.model.response.FileUploadToken;

public interface MediaService {
    FileUploadToken getUploadToken();

    String uploadFile(String upToken);
}
