package cn.akfang.berry.service;

import cn.akfang.berry.common.feign.client.VideoClient;
import cn.akfang.berry.common.model.dto.EsVideoMetaDTO;
import cn.akfang.berry.common.model.response.VideoVO;
import cn.akfang.berry.mapper.VideoEsMapper;
import cn.akfang.berry.model.VideoEsPO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.dromara.easyes.core.core.EsWrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EsVideoService {

    @Autowired
    private VideoClient videoClient;

    @Autowired
    private VideoEsMapper videoEsMapper;
    

    public Page<VideoVO> selectVideoVOByKeywordPage(String keyword, int currentPageNum, Long currentUserId) {
        LambdaEsQueryWrapper<VideoEsPO> like = EsWrappers.lambdaQuery(VideoEsPO.class)
                .match(VideoEsPO::getContent, keyword)
                .sortByScore();
//                .or().match(VideoEsPO::getTags, keyword);
        EsPageInfo<VideoEsPO> videoEsPOEsPageInfo = videoEsMapper.pageQuery(like, currentPageNum, 10);
        Map<Long, VideoEsPO> videoEsPOMap = videoEsPOEsPageInfo.getList().stream()
                .collect(Collectors.toMap(VideoEsPO::getVideoId, item -> item));
        Page<VideoVO> videoEsSearchDTOPage = new Page<>();

        videoEsSearchDTOPage.setTotal(videoEsPOEsPageInfo.getTotal());
        videoEsSearchDTOPage.setCurrent(videoEsPOEsPageInfo.getPageNum());

        List<Long> videoIds = videoEsPOEsPageInfo.getList().stream().map(VideoEsPO::getVideoId)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(videoIds)) {
            videoEsSearchDTOPage.setRecords(CollectionUtil.newArrayList());
        } else {
            List<VideoVO> videoVOS = videoClient.listVideoVoByIds(videoIds, String.valueOf(currentUserId)).stream()
                    .peek(item -> {
                        VideoEsPO esPO = videoEsPOMap.get(item.getId());
                        EsVideoMetaDTO esVideoMetaDTO = new EsVideoMetaDTO();
                        BeanUtil.copyProperties(esPO, esVideoMetaDTO);
                        item.setSearchMeta(esVideoMetaDTO);
                    }).collect(Collectors.toList());

            videoEsSearchDTOPage.setRecords(videoVOS);
        }
        return videoEsSearchDTOPage;
    }
}
