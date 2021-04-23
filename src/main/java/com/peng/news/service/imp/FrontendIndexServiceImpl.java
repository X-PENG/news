package com.peng.news.service.imp;

import com.peng.news.mapper.NewsColumnMapper;
import com.peng.news.model.vo.NewsColumnVO;
import com.peng.news.service.FrontendIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PENG
 * @version 1.0
 * @date 2021/4/23 16:33
 */
@Service
public class FrontendIndexServiceImpl implements FrontendIndexService {

    @Autowired
    NewsColumnMapper newsColumnMapper;


    @Override
    public List<NewsColumnVO> allEnabledOneLevelColsOrderByMenuOrder() {
        return newsColumnMapper.getEnabledChildrenNewsColumnListByParentId(null);
    }
}
