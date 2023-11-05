package cn.akfang.berry.common.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

@Service
@FeignClient(value = "berry-search-service")
public interface SearchClient {

}
