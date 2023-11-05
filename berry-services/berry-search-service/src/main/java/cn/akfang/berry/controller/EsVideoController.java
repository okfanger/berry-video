package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.feign.client.VideoClient;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.VideoVO;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.mapper.VideoEsMapper;
import cn.akfang.berry.model.VideoEsPO;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.dromara.easyes.core.core.EsWrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/video")
@Slf4j
public class EsVideoController {

    @Autowired
    VideoEsMapper videoEsMapper;

    @Autowired
    VideoClient videoClient;
    @GetMapping("")
    public BaseResponse<Page<VideoVO>> getVideoIdsByKeyword(
            @RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "current", defaultValue = "1") String currentStr) {

        LambdaEsQueryWrapper<VideoEsPO> like = EsWrappers.lambdaQuery(VideoEsPO.class)
                .match(VideoEsPO::getContent, keyword);
//                .or().match(VideoEsPO::getTags, keyword);

        EsPageInfo<VideoEsPO> videoEsPOEsPageInfo = videoEsMapper.pageQuery(like, NumberUtil.parseInt(currentStr), 10);
        Page<VideoVO> videoEsSearchDTOPage = new Page<>();
        videoEsSearchDTOPage.setTotal(videoEsPOEsPageInfo.getTotal());
        videoEsSearchDTOPage.setCurrent(videoEsPOEsPageInfo.getPageNum());
        videoEsSearchDTOPage.setTotal(videoEsPOEsPageInfo.getTotal());
        List<Long> videoIds = videoEsPOEsPageInfo.getList().stream().map(VideoEsPO::getVideoId)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(videoIds)) {
            videoEsSearchDTOPage.setRecords(CollectionUtil.newArrayList());
        } else {
//            videoEsSearchDTOPage.setRecords(videoClient.getVOByIds(videoIds, NumberUtil.parseLong(userId)));
        }
        return ResultUtils.success(videoEsSearchDTOPage);
    }


//    @Autowired
//    private RestHighLevelClient restHighLevelClient;
//    @Override
//    public String ikAnalyzer(String words) {
////        IndicesClient indices = restHighLevelClient.indices();
//////        AnalyzeRequestBuilder request = new AnalyzeRequestBuilder(indices, ,"中华人民共和国国歌");
////// request.setAnalyzer("ik");
////        request.setTokenizer("ik");
////// Analyzer（分析器）、Tokenizer（分词器）
////        List listAnalysis = request.execute().actionGet().getTokens();
////        System.out.println(listAnalysis);
//// listAnalysis中的结果就是分词的结果
//        return "success";
//    }
}
