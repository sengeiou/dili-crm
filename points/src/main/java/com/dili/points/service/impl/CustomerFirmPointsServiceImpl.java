package com.dili.points.service.impl;

import com.dili.points.dao.CustomerFirmPointsMapper;
import com.dili.points.domain.CustomerFirmPoints;
import com.dili.points.service.CustomerFirmPointsService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-07-30 16:20:03.
 */
@Service
public class CustomerFirmPointsServiceImpl extends BaseServiceImpl<CustomerFirmPoints, Long> implements CustomerFirmPointsService {

    public CustomerFirmPointsMapper getActualDao() {
        return (CustomerFirmPointsMapper)getDao();
    }
}