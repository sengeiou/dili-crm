package com.dili.crm.service.impl;

import com.dili.crm.dao.CustomerMapper;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.dto.CustomerChartDTO;
import com.dili.crm.service.CacheService;
import com.dili.crm.service.CustomerService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-13 17:11:22.
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {

    public CustomerMapper getActualDao() {
        return (CustomerMapper)getDao();
    }

    @Autowired
    private CacheService cacheService;

    @Override
    public List<Customer> list(Customer condtion) {
        condtion.setYn(1);
        return super.list(condtion);
    }

    @Override
    public EasyuiPageOutput listEasyuiPageByExample(Customer domain, boolean useProvider) throws Exception {
        cacheService.refreshDepartment();
        domain.setYn(1);
        return super.listEasyuiPageByExample(domain, useProvider);
    }

    public BaseOutput insertSelectiveWithOutput(Customer customer) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("新增失败，登录超时");
        }
        customer.setCreatedId(userTicket.getId());
        customer.setOwnerId(userTicket.getId());
        super.insertSelective(customer);
        return BaseOutput.success("新增成功");
    }

    @Override
    public BaseOutput updateSelectiveWithOutput(Customer condtion) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("修改失败，登录超时");
        }
        condtion.setModifiedId(userTicket.getId());
        super.updateSelective(condtion);
        return BaseOutput.success("修改成功");
    }

    @Override
    public BaseOutput deleteWithOutput(Long id) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("删除失败，登录超时");
        }
        Customer condtion = DTOUtils.newDTO(Customer.class);
        condtion.setModifiedId(userTicket.getId());
        condtion.setYn(0);
        condtion.setId(id);
        super.updateSelective(condtion);
        return BaseOutput.success("删除成功");
    }

	@Override
	public BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByType() {
		return BaseOutput.success().setData(this.getActualDao().selectCustomersGroupByType());
	}

	@Override
	public BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByMarket() {
		return BaseOutput.<List<CustomerChartDTO>>success().<List<CustomerChartDTO>>setData(this.getActualDao().selectCustomersGroupByMarket());
	}

	@Override
	public BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByProfession() {
		return BaseOutput.success().setData(this.getActualDao().selectCustomersGroupByProfession());
	}
}