package com.dili.crm.service.impl;

import com.dili.crm.cache.CrmCache;
import com.dili.crm.dao.CityMapper;
import com.dili.crm.domain.City;
import com.dili.crm.service.CityService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-14 15:11:53.
 */
@Service
public class CityServiceImpl extends BaseServiceImpl<City, Long> implements CityService {

    public CityMapper getActualDao() {
        return (CityMapper)getDao();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSelective(City city) {
        int result =  super.insertSelective(city);
        CrmCache.CITY_MAP.put(city.getId(),city);
        return result;
    }
}