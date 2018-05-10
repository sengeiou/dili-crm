package com.dili.points.service.impl;

import com.dili.points.dao.CustomerCategoryPointsMapper;
import com.dili.points.domain.CustomerCategoryPoints;
import com.dili.points.service.CustomerCategoryPointsService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-09 17:35:45.
 */
@Service
public class CustomerCategoryPointsServiceImpl extends BaseServiceImpl<CustomerCategoryPoints, Long> implements CustomerCategoryPointsService {

    public CustomerCategoryPointsMapper getActualDao() {
        return (CustomerCategoryPointsMapper)getDao();
    }
}