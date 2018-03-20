package com.dili.points.service.impl;

import com.dili.points.dao.ExchangeCommoditiesMapper;
import com.dili.points.domain.ExchangeCommodities;
import com.dili.points.service.ExchangeCommoditiesService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Service
public class ExchangeCommoditiesServiceImpl extends BaseServiceImpl<ExchangeCommodities, Long> implements ExchangeCommoditiesService {

    public ExchangeCommoditiesMapper getActualDao() {
        return (ExchangeCommoditiesMapper)getDao();
    }
}