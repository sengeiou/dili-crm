package com.dili.alm.service.impl;

import com.dili.alm.dao.SystemConfigMapper;
import com.dili.alm.domain.SystemConfig;
import com.dili.alm.service.SystemConfigService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-06-03 14:35:19.
 */
@Service
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig, Long> implements SystemConfigService {

    public SystemConfigMapper getActualDao() {
        return (SystemConfigMapper)getDao();
    }
}