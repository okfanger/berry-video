package cn.akfang.berry.service;

import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.model.response.IkAnalyzerResponse;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EsCommonService {

    @Value("${easy-es.address}")
    private String address;

    public IkAnalyzerResponse ikAnalyzer(String analyzer, String words) {
        if (StrUtil.isBlank(analyzer)) {
            analyzer = "ik_min_word";
        }
        String url = "http://" + address + "/_analyze";
        Map<String, String> bodyMap = MapUtil.builder("analyzer", analyzer)
                .put("text", words).build();
        HttpRequest request = HttpRequest.post(url).body(JSONUtil.toJsonPrettyStr(bodyMap));
        HttpResponse response = null;
        try {
            response = request.execute();
            if (response.isOk()) {
                String responseBody = response.body();
                return JSONUtil.toBean(responseBody, IkAnalyzerResponse.class);
            } else {
                throw new BerryRpcException(ErrorCode.SYSTEM_ERROR, "IK分词器调用失败");
            }
        } finally {
            if (ObjectUtil.isNotNull(response)) {
                response.close();
            }
        }
    }

//    public Set<String> ikAnalyzerAndGetWordSet(String words) {
//        IkAnalyzerResponse ikAnalyzerResponse = ikAnalyzer(words);
//        ikAnalyzerResponse.getTokens()
//                .stream().map(IkAnalyzerResponse.Tokens::getToken)
//                .filter()
//    }

}
