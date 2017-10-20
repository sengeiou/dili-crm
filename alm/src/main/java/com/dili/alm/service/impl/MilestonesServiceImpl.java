package com.dili.alm.service.impl;

import com.dili.alm.dao.MilestonesMapper;
import com.dili.alm.domain.Milestones;
import com.dili.alm.service.MilestonesService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-10-20 11:02:17.
 */
@Service
public class MilestonesServiceImpl extends BaseServiceImpl<Milestones, Long> implements MilestonesService {

    public MilestonesMapper getActualDao() {
        return (MilestonesMapper)getDao();
    }
}