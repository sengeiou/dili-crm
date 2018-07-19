package com.dili.crm.service.impl;

import com.dili.crm.dao.CustomerVisitMapper;
import com.dili.crm.domain.CustomerVisit;
import com.dili.crm.domain.dto.CustomerVisitChartDTO;
import com.dili.crm.service.BizNumberService;
import com.dili.crm.service.CustomerVisitService;
import com.dili.crm.service.FirmService;
import com.dili.crm.service.VisitEventService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
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

    @Autowired
    private BizNumberService bizNumberService;
    @Autowired
    FirmService firmService;
    
    @Override
    public BaseOutput insertSelectiveWithOutput(CustomerVisit customerVisit) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("新增失败，登录超时");
        }
        customerVisit.setCreatedId(userTicket.getId());
        customerVisit.setModifiedId(userTicket.getId());
        customerVisit.setCode(bizNumberService.getCustomerVisitCode());
        super.insertSelective(customerVisit);
        return BaseOutput.success("新增成功").setData(customerVisit);
    }

	@Override
	public BaseOutput<List<CustomerVisitChartDTO>> selectCustomerVisitGroupByMode(String firmCode) {
		List<String>firmCodes = this.firmService.getCurrentUserAvaliableFirmCodes(firmCode);
		if(firmCodes.isEmpty()) {
			return new BaseOutput<>().setData(Collections.emptyList());
		}
		return BaseOutput.success().setData(this.getActualDao().selectCustomerVisitGroupByMode(firmCodes));
	}

	@Override
	public BaseOutput<List<CustomerVisitChartDTO>> selectCustomerVisitGroupByState(String firmCode) {
		List<String>firmCodes = this.firmService.getCurrentUserAvaliableFirmCodes(firmCode);
		if(firmCodes.isEmpty()) {
			return new BaseOutput<>().setData(Collections.emptyList());
		}
		return BaseOutput.success().setData(this.getActualDao().selectCustomerVisitGroupByState(firmCodes));
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput updateSelectiveWithOutput(CustomerVisit customerVisit) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("新增失败，登录超时");
        }
        customerVisit.setModifiedId(userTicket.getId());
        CustomerVisit old = get(customerVisit.getId());
        customerVisit = DTOUtils.link(customerVisit, old, CustomerVisit.class);
        Example example = new Example(CustomerVisit.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",customerVisit.getId());
        if (null != customerVisit.getModified()){
            criteria.andEqualTo("modified",customerVisit.getModified());
        }
        //状态不能为3(已完成)
        criteria.andNotEqualTo("state",3);
        customerVisit.setModified(new Date());
        int i= getActualDao().updateByExample(customerVisit,example);
        if(i > 0){
            return BaseOutput.success("更新成功").setData(customerVisit);
        }
        return BaseOutput.failure("更新失败，数据已变更");
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