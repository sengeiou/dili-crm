package com.dili.points.service.impl;

import com.dili.points.dao.CustomerPointsMapper;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.service.CustomerPointsService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:30.
 */
@Service
public class CustomerPointsServiceImpl extends BaseServiceImpl<CustomerPoints, Long> implements CustomerPointsService {

    public CustomerPointsMapper getActualDao() {
        return (CustomerPointsMapper)getDao();
    }
}