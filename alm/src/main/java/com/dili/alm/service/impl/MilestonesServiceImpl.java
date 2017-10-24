package com.dili.alm.service.impl;

import com.dili.alm.dao.MilestonesMapper;
import com.dili.alm.domain.Milestones;
import com.dili.alm.service.MilestonesService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-10-20 11:02:17.
 */
@Service
public class MilestonesServiceImpl extends BaseServiceImpl<Milestones, Long> implements MilestonesService {

    public MilestonesMapper getActualDao() {
        return (MilestonesMapper)getDao();
    }

    @Override
    public int insertSelective(Milestones milestones) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        milestones.setPublishMemberId(userTicket.getId());
        milestones.setCreated(new Date());
        return super.insertSelective(milestones);
    }

    @Override
    public int updateSelective(Milestones milestones) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        milestones.setModifyMemberId(userTicket.getId());
        milestones.setModified(new Date());
        return super.updateSelective(milestones);
    }
}