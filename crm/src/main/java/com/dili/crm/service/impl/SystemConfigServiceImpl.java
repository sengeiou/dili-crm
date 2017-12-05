package com.dili.crm.service.impl;

import com.dili.crm.dao.SystemConfigMapper;
import com.dili.crm.domain.SystemConfig;
import com.dili.crm.service.SystemConfigService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-12-05 14:23:24.
 */
@Service
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig, Long> implements SystemConfigService {

    public SystemConfigMapper getActualDao() {
        return (SystemConfigMapper)getDao();
    }
}