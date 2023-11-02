package cn.akfang.berry.common.feign.client;


import cn.akfang.berry.common.model.entity.FilePO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "berry-misc-service")
public interface MiscClient {
    @GetMapping("/file/getById")
    FilePO getFileById(@RequestParam("fileId") Long fileId);

}
