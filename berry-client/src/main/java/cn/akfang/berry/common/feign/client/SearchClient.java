package cn.akfang.berry.common.feign.client;

import cn.akfang.berry.common.model.response.IkAnalyzerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(value = "berry-search-service")
public interface SearchClient {

    @PostMapping("/ik")
    IkAnalyzerResponse ikAnalyzer(@RequestBody String words);
}
