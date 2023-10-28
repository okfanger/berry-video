package cn.akfang.berry.common.feign.client;


import cn.akfang.berry.common.model.dto.WxLoginTmpTicketDTO;
import cn.akfang.berry.common.model.response.FileUploadToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "berry-misc-service")
public interface MiscService {
    @GetMapping("/media/ticket")
    WxLoginTmpTicketDTO getWxLoginTicketInfo(@RequestParam("sceneStr") String sceneStr,
                                             @RequestParam("expireTime") Integer expireTime);

    @PostMapping("/media/reply")
    String reply(@RequestBody String requestXmlBody);

    @GetMapping("/media/checkSignature")
    Boolean checkSignature(@RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam("signatrue") String signature);

    @GetMapping("/wx/getUploadToken")
    FileUploadToken getUploadToken();

    @GetMapping("/wx/uploadFile")
    String uploadFile(@RequestParam("upToken") String upToken);
}
