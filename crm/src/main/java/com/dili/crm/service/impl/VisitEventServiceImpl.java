package com.dili.crm.service.impl;

import com.dili.crm.dao.VisitEventMapper;
import com.dili.crm.domain.CustomerVisit;
import com.dili.crm.domain.VisitEvent;
import com.dili.crm.service.CustomerVisitService;
import com.dili.crm.service.VisitEventService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-21 16:39:42.
 */
@Service
public class VisitEventServiceImpl extends BaseServiceImpl<VisitEvent, Long> implements VisitEventService {

    public VisitEventMapper getActualDao() {
        return (VisitEventMapper)getDao();
    }

    @Resource
    private CustomerVisitService customerVisitService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteByVisit(Long visitId) {
        VisitEvent visitEvent = DTOUtils.newDTO(VisitEvent.class);
        visitEvent.setCustomerVisitId(visitId);
        return getActualDao().delete(visitEvent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput insertSelectiveWithOut(VisitEvent visitEvent) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("更新失败，登录超时");
        }
        CustomerVisit visit = customerVisitService.get(visitEvent.getCustomerVisitId());
        if (3==visit.getState()){
            return BaseOutput.failure("更新失败，回访已完成");
        }
        if (1==visit.getState()){
            //更新回访状态为进行中
            visit.setState(2);
            visit.setModifiedId(userTicket.getId());
            customerVisitService.updateSelectiveWithOutput(visit);
        }
        visitEvent.setUserId(userTicket.getId());
        super.insertSelective(visitEvent);
        return BaseOutput.success("插入成功");
    }
}