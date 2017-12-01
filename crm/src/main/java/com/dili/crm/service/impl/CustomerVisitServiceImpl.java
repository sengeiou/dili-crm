package com.dili.crm.service.impl;

import com.dili.crm.dao.CustomerVisitMapper;
import com.dili.crm.domain.CustomerVisit;
import com.dili.crm.domain.VisitEvent;
import com.dili.crm.domain.dto.CustomerVisitChartDTO;
import com.dili.crm.service.CustomerVisitService;
import com.dili.crm.service.VisitEventService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-21 17:27:41.
 */
@Service
public class CustomerVisitServiceImpl extends BaseServiceImpl<CustomerVisit, Long> implements CustomerVisitService {

    public CustomerVisitMapper getActualDao() {
        return (CustomerVisitMapper)getDao();
    }
    @Resource
    private VisitEventService visitEventService;

    @Override
    public BaseOutput insertSelectiveWithOutput(CustomerVisit customerVisit) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("新增失败，登录超时");
        }
        customerVisit.setCreatedId(userTicket.getId());
        customerVisit.setModifiedId(userTicket.getId());
        super.insertSelective(customerVisit);
        return BaseOutput.success("新增成功");
    }

	@Override
	public BaseOutput<List<CustomerVisitChartDTO>> selectCustomerVisitGroupByMode() {
		return BaseOutput.success().setData(this.getActualDao().selectCustomerVisitGroupByMode());
	}

	@Override
	public BaseOutput<List<CustomerVisitChartDTO>> selectCustomerVisitGroupByState() {
		return BaseOutput.success().setData(this.getActualDao().selectCustomerVisitGroupByState());
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput updateSelectiveWithOutput(CustomerVisit customerVisit) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("新增失败，登录超时");
        }
        if (null != customerVisit.getModified()){
            Example example = new Example(CustomerVisit.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("modified",customerVisit.getModified());
            getActualDao().updateByExampleSelective(customerVisit,example);
        }else{
            super.updateSelective(customerVisit);
        }
        return BaseOutput.success("更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput deleteAndEvent(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("新增失败，登录超时");
        }
        super.delete(id);
        visitEventService.deleteByVisit(id);
        return BaseOutput.success("删除成功");
    }

}