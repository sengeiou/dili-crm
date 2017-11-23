package com.dili.crm.service.impl;

import com.dili.crm.dao.CustomerVisitMapper;
import com.dili.crm.domain.CustomerVisit;
import com.dili.crm.service.CustomerVisitService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-21 17:27:41.
 */
@Service
public class CustomerVisitServiceImpl extends BaseServiceImpl<CustomerVisit, Long> implements CustomerVisitService {

    public CustomerVisitMapper getActualDao() {
        return (CustomerVisitMapper)getDao();
    }

    @Override
    public BaseOutput insertSelectiveWithOutput(CustomerVisit customerVisit) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("新增失败，登录超时");
        }
        customerVisit.setCreatedId(userTicket.getId());
        super.insertSelective(customerVisit);
        return BaseOutput.success("新增成功");
    }
}