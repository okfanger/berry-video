package cn.akfang.berry.service;

import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.mapper.UserEsMapper;
import cn.akfang.berry.model.UserEsPO;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.dromara.easyes.core.core.EsWrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserEsService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private UserEsMapper userEsMapper;

    public Page<UserBaseVO> selectUserBaseVOByKeywordPage(String keyword, int currentPageNum, Long currentUserId) {
        LambdaEsQueryWrapper<UserEsPO> like = EsWrappers.lambdaQuery(UserEsPO.class)
                .match(UserEsPO::getNickName, keyword);

        EsPageInfo<UserEsPO> userEsPOEsPageInfo = userEsMapper.pageQuery(like, currentPageNum, 10);

        Page<UserBaseVO> userEsPage = new Page<>();
        userEsPage.setTotal(userEsPOEsPageInfo.getTotal());
        userEsPage.setCurrent(userEsPOEsPageInfo.getPageNum());
        userEsPage.setSize(userEsPOEsPageInfo.getPageSize());

        List<Long> userIds = userEsPOEsPageInfo.getList().stream().map(UserEsPO::getUserId)
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(userIds)) {
            userEsPage.setRecords(CollectionUtil.newArrayList());
        } else {
            userEsPage.setRecords(userClient.getUserBaseVOListByIds(userIds, currentUserId));
        }
        return userEsPage;
    }
}
