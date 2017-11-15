package com.dili.crm.service.impl;

import com.dili.crm.dao.VehicleMapper;
import com.dili.crm.domain.Vehicle;
import com.dili.crm.service.VehicleService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-15 11:16:50.
 */
@Service
public class VehicleServiceImpl extends BaseServiceImpl<Vehicle, Long> implements VehicleService {

    public VehicleMapper getActualDao() {
        return (VehicleMapper)getDao();
    }
}