package com.dili.crm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.crm.dao.CustomerMapper;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.dto.CustomerChartDTO;
import com.dili.crm.domain.dto.MembersDto;
import com.dili.crm.service.CacheService;
import com.dili.crm.service.CustomerService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public BaseOutput insertSelectiveWithOutput(Customer customer) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("新增失败，登录超时");
        }
	    Customer condition = DTOUtils.newDTO(Customer.class);
        condition.setCertificateType(customer.getCertificateType());
        condition.setCertificateNumber(customer.getCertificateNumber());
        condition.setYn(1);
        List<Customer> list = list(condition);
        if(!list.isEmpty()){
        	return BaseOutput.failure("证件号码已存在");
        }
        customer.setCreatedId(userTicket.getId());
        customer.setOwnerId(userTicket.getId());
        super.insertSelective(customer);
        return BaseOutput.success("新增成功").setData(customer);
    }

    @Override
    public BaseOutput updateSelectiveWithOutput(Customer customer) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("修改失败，登录超时");
        }
        customer.setModifiedId(userTicket.getId());
        super.updateSelective(customer);
        return BaseOutput.success("修改成功").setData(customer);
    }

	@Override
	public BaseOutput updateWithOutput(Customer customer) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null){
			return BaseOutput.failure("修改失败，登录超时");
		}
		customer.setModifiedId(userTicket.getId());
		super.update(customer);
		return BaseOutput.success("修改成功").setData(customer);
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

	@Override
	public void handleDetail(ModelMap modelMap, Long id) throws Exception {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null){
			throw new RuntimeException("未登录");
		}
		//页面上用于展示拥有者和新增时获取拥有者id
		modelMap.put("user", userTicket);

		Customer customer = get(id);
		if(customer == null){
			throw new RuntimeException("根据id["+id+"]找不到对应客户");
		}
		List<Map> list = ValueProviderUtils.buildDataByProvider(getCustomerMetadata(), Lists.newArrayList(customer));
		modelMap.put("customer", JSONObject.toJSONString(list.get(0)));
		if(customer.getParentId() != null){
			Customer parent = get(customer.getParentId());
			modelMap.put("parentCustomer", parent);
		}
	}

	@Override
    public String listMembersPage(MembersDto membersDto) throws Exception {
        String parentIdsStr = getActualDao().getParentCustomers(membersDto.getId());
        if(StringUtils.isBlank(parentIdsStr)) return null;
        List<String> parentIds = Arrays.asList(parentIdsStr.split(","));
	    membersDto.setIdNotIn(parentIds);
        membersDto.setId(null);
        //由于查询条件中有or parent_id is null，所以这里只能自己构建Example了，以后利刃框架会支持or查询
	    Example example = new Example(Customer.class);
	    //这里构建name和id not in的等条件部分
	    buildExampleByGetterMethods(membersDto, example);
	    Example.Criteria criteria = example.getOredCriteria().get(0);
	    criteria.andCondition("(`parent_id` != '1' or `parent_id` is null)");
	    //设置分页信息
	    Integer page = membersDto.getPage();
	    page = (page == null) ? Integer.valueOf(1) : page;
	    if(membersDto.getRows() != null && membersDto.getRows() >= 1) {
		    //为了线程安全,请勿改动下面两行代码的顺序
		    PageHelper.startPage(page, membersDto.getRows());
	    }
	    List<Customer> list = getActualDao().selectByExample(example);
	    long total = list instanceof Page ? ((Page) list).getTotal() : list.size();
	    return new EasyuiPageOutput(Integer.parseInt(String.valueOf(total)), ValueProviderUtils.buildDataByProvider(membersDto, list)).toString();
    }

	@Override
	public BaseOutput deleteMembers(Long id){
		Customer customer = get(id);
		customer.setParentId(null);
		return updateWithOutput(customer);
	}

	/**
	 * 由于无法获取到表头上的meta信息，展示客户详情只有id参数，所以需要在后台构建
	 * @return
	 */
	private Map getCustomerMetadata(){
		Map<Object, Object> metadata = new HashMap<>();
		JSONObject ownerProvider = new JSONObject();
		ownerProvider.put("provider", "ownerProvider");
		metadata.put("ownerId", ownerProvider);
		JSONObject cityProvider = new JSONObject();
		cityProvider.put("provider", "cityProvider");
		metadata.put("operatingArea", cityProvider);
		metadata.put("certificateType", getDDProvider(3L));
		metadata.put("organizationType", getDDProvider(5L));
		metadata.put("market", getDDProvider(2L));
		metadata.put("type", getDDProvider(4L));
		metadata.put("profession", getDDProvider(6L));
		metadata.put("certificateTime", getDatetimeProvider());
		metadata.put("created", getDatetimeProvider());
		return metadata;
	}

	//获取时间提供者
	private JSONObject getDatetimeProvider(){
		JSONObject datetimeProvider = new JSONObject();
		datetimeProvider.put("provider", "datetimeProvider");
		return datetimeProvider;
	}
	//获取数据字典提供者
	private JSONObject getDDProvider(Long ddId){
		JSONObject dataDictionaryValueProvider = new JSONObject();
		dataDictionaryValueProvider.put("provider", "dataDictionaryValueProvider");
		dataDictionaryValueProvider.put("queryParams", "{dd_id:"+ddId+"}");
		return dataDictionaryValueProvider;
	}

}