package cn.akfang.berry.controller;

import cn.akfang.berry.common.feign.client.SearchClient;
import cn.akfang.berry.common.model.dto.IKAnalyzerRequest;
import cn.akfang.berry.common.model.response.IkAnalyzerResponse;
import cn.akfang.berry.service.EsCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@Slf4j
public class SearchController implements SearchClient {


    @Autowired
    private EsCommonService esCommonService;

    @Override
    public IkAnalyzerResponse ikAnalyzer(IKAnalyzerRequest request) {
        return esCommonService.ikAnalyzer("ik_smart", request.getWords());
    }
}
