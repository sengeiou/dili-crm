package com.dili.crm.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.crm.dao.CustomerMapper;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.Department;
import com.dili.crm.domain.User;
import com.dili.crm.domain.dto.*;
import com.dili.crm.rpc.CustomerPointsRpc;
import com.dili.crm.rpc.UserRpc;
import com.dili.crm.service.CacheService;
import com.dili.crm.service.ChartService;
import com.dili.crm.service.CustomerService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired ChartService chartService;

	@Autowired
	private UserRpc userRpc;

	@Autowired
	private CustomerPointsRpc customerPointsRpc;

    @Override
    public List<Customer> list(Customer condtion) {
	    condtion.setYn(1);
	    return super.list(condtion);
    }

    @Override
    public EasyuiPageOutput listEasyuiPageByExample(Customer domain, boolean useProvider) throws Exception {
        cacheService.refreshDepartment();
	    DTO queryDto = DTOUtils.go(domain);
	    CustomerTreeDto customerTreeDto= DTOUtils.as(domain, CustomerTreeDto.class);
	    //如果只有sort和order字段,即没有查询条件的情况下，就只查parentId为空的
	    if(queryDto.size() <= getDefaultParamSize(domain)){
		    //全查时查顶级
		    customerTreeDto.mset(IDTO.NULL_VALUE_FIELD, "parent_id");
	    }
	    domain.setYn(1);
	    EasyuiPageOutput easyuiPageOutput = super.listEasyuiPageByExample(customerTreeDto, useProvider);
	    for(Object rowObj : easyuiPageOutput.getRows()) {
		    if(DTOUtils.isDTOProxy(rowObj)){
			    DTOUtils.as(rowObj, CityDto.class).setState("closed");
		    }else {
			    Map rowMap = (Map) rowObj;
			    rowMap.put("state", "closed");
		    }
	    }
	    return easyuiPageOutput;
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
        //本系统新增的，写死为crm，对应数据字典
	    customer.setSourceSystem("crm");
        customer.setCreatedId(userTicket.getId());
        customer.setOwnerId(userTicket.getId());
        //查询当前用户所属的部门
		BaseOutput<List<Department>> listBaseOutput = userRpc.listUserDepartmentByUserId(userTicket.getId());
		List<Department> departments = listBaseOutput.getData();
		if (CollectionUtils.isNotEmpty(departments)){
			customer.setDepartment(String.valueOf(departments.get(0).getId()));
		}
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
		customer.setModified(new Date());
        boolean hasParent = customer.getParentId() == null;
		customer = DTOUtils.link(customer, get(customer.getId()), Customer.class);
        if(hasParent){
			customer.setParentId(null);
		}
        super.update(customer);
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
    public BaseOutput deleteWithOutput(Long id) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("删除失败，登录超时");
        }
		Customer query = DTOUtils.newDTO(Customer.class);
        query.setParentId(id);
        query.setYn(1);
		EasyuiPageOutput pageOutput = super.listEasyuiPageByExample(query, false);
		if (pageOutput.getTotal() > 0){
			return BaseOutput.failure("删除失败，该客户存在子客户");
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
		Map customerMap = list.get(0);
		if(customerMap.get("createdId") != null){
			BaseOutput<User> userBaseOutput = userRpc.get(Long.parseLong(customerMap.get("createdId").toString()));
			if(userBaseOutput.isSuccess()) {
				customerMap.put("createdName", userRpc.get(Long.parseLong(customerMap.get("createdId").toString())).getData().getRealName());
			}
		}
		//查询客户可用积分
		CustomerPointsApiDTO customerPointsApiDTO = DTOUtils.newDTO(CustomerPointsApiDTO.class);
		customerPointsApiDTO.setIds(Lists.newArrayList(customer.getId().toString()));
		BaseOutput<List<CustomerPoints>> output = customerPointsRpc.listCustomerPoints(customerPointsApiDTO);
		if(output.isSuccess() && !output.getData().isEmpty()){
			customerMap.put("available", output.getData().get(0).getAvailable());
			modelMap.put("available", output.getData().get(0).getAvailable());
		}else {
			customerMap.put("available", 0);
			modelMap.put("available", 0);
		}
		modelMap.put("customerId", customerMap.get("id"));
		modelMap.put("certificateNumber", customerMap.get("certificateNumber"));
		modelMap.put("customer", JSONObject.toJSONString(customerMap));
	    modelMap.put("startDate",this.calStartDate());
	    modelMap.put("endDate",this.calEndDate());
	    
	    modelMap.put("clientPurchasingTopChartUrl",this.chartService.getClientPurchasingTopChartUrl());
	    modelMap.put("clientSalesTopChartUrl",this.chartService.getClientSalesTopChartUrl());
	    modelMap.put("clientUserContributionChartUrl",this.chartService.getClientUserContributionChartUrl());
		if(customer.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX+"parentId") != null){
			Customer parent = get(Long.parseLong(customer.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX+"parentId").toString()));
			modelMap.put("parentCustomer", parent);
		}
	}

	@Override
    public String listMembersPage(MembersDto membersDto) throws Exception {
        String parentIdsStr = getActualDao().getParentCustomers(membersDto.getId());
        if(StringUtils.isBlank(parentIdsStr)) {
        	return null;
        }
        Long id = membersDto.getId();
        List<String> parentIds = Arrays.asList(parentIdsStr.split(","));
	    membersDto.setIdNotIn(parentIds);
        membersDto.setId(null);
        //由于查询条件中有or parent_id is null，所以这里只能自己构建Example了，以后利刃框架会支持or查询
	    Example example = new Example(Customer.class);
	    //这里构建name和id not in的等条件部分
	    buildExampleByGetterMethods(membersDto, example);
	    Example.Criteria criteria = example.getOredCriteria().get(0);
		criteria.andEqualTo("yn",1);
	    criteria.andCondition("(`parent_id` != '"+id+"' or `parent_id` is null)");
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


	@Override
	public String listParentCustomerPage(MembersDto membersDto) throws Exception {
		String childIdsStr = getActualDao().getChildCustomerIds(membersDto.getId());
		if(StringUtils.isBlank(childIdsStr)) {
			return null;
		}
		List<String> childIds = Arrays.asList(childIdsStr.split(","));
		membersDto.setIdNotIn(childIds);
		membersDto.setId(null);
		membersDto.setYn(1);
		return super.listEasyuiPageByExample(membersDto, true).toString();
	}

	@Override
	public String expandEasyuiPageByParentId(Long parentId) throws Exception {
		Customer customer = DTOUtils.newDTO(Customer.class);
		customer.setParentId(parentId);
		Map<String, Object> metadata = new HashMap<>();
		JSONObject ownerProvider = new JSONObject();
		ownerProvider.put("provider", "ownerProvider");
		metadata.put("ownerId", ownerProvider);
		JSONObject departmentProvider = new JSONObject();
		departmentProvider.put("provider", "departmentProvider");
		metadata.put("department", departmentProvider);
		metadata.put("created", getDatetimeProvider());
		metadata.put("modified", getDatetimeProvider());
		metadata.put("certificateType", getDDProvider(3L));
		metadata.put("organizationType", getDDProvider(5L));
		JSONObject cityProvider = new JSONObject();
		cityProvider.put("provider", "cityProvider");
		metadata.put("operatingArea", cityProvider);
		metadata.put("sourceSystem", getDDProvider(8L));
		metadata.put("market", getDDProvider(2L));
		metadata.put("type", getDDProvider(4L));
		metadata.put("profession", getDDProvider(6L));

		customer.mset(metadata);
		EasyuiPageOutput easyuiPageOutput = this.listEasyuiPageByExample(customer, true);
		for(Object rowObj : easyuiPageOutput.getRows()) {
			if(DTOUtils.isDTOProxy(rowObj)){
				DTOUtils.as(rowObj, CustomerTreeDto.class).setState("closed");
			}else {
				Map rowMap = (Map) rowObj;
				rowMap.put("state", "closed");
			}
		}
		return JSONArray.toJSONString(easyuiPageOutput.getRows());
	}

    @Override
    public List<CustomerAddressDto> getCustomerAddress(){
        return getActualDao().selectCustomerAddress();
    }

	/**
	 * 根据客户类型获取客户的经营地区相关信息
	 *
	 * @param types 客户类型：采购、销售、代买等
	 * @return
	 */
	@Override
	public List<Customer> listCustomerOperating(Set<String> types) {
		Example example = new Example(Customer.class);
		Example.Criteria criteria = example.createCriteria();
		/***  构造查询条件 ***/
		//经纬度不能为空
		criteria.andIsNotNull("operatingLng").andIsNotNull("operatingLat");
		//状态为 可用
		criteria.andCondition("yn=1");
		if (CollectionUtils.isNotEmpty(types)){
			StringBuilder condition = new StringBuilder();
			condition.append("(");
			types.forEach(n ->{
				 if ("other".equals(n.toLowerCase())){
					 condition.append(" type is null or");
				 }else{
					 condition.append(" type ='").append(n).append("' or");
				 }
			});
			int or = condition.lastIndexOf("or");
			condition.delete(or,or+2);
			condition.append(" )");
			criteria.andCondition(condition.toString());
		}
		return getActualDao().selectByExample(example);
	}


	// ========================================== 私有方法 ==========================================

	/**
	 * 获取实体的默认参数长度
	 * @param domain
	 * @return
	 */
	private int getDefaultParamSize(Customer domain){
		int size = 0;
		if(domain.getPage() != null){
			size ++;
		}
		if(domain.getRows() != null){
			size ++;
		}
		if(domain.getOrder() != null){
			size++;
		}
		if(domain.getSort() != null){
			size++;
		}
		return size;
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
		//回访对象
		JSONObject customerProvider = new JSONObject();
		customerProvider.put("provider", "customerProvider");
		metadata.put("parentId", customerProvider);
		//部门信息
		JSONObject departmentProvider = new JSONObject();
		departmentProvider.put("provider", "departmentProvider");
		metadata.put("department", departmentProvider);
		//字典
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
	private String calStartDate() {

		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.MONDAY, -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(cal.getTime());
	}
	
	private String calEndDate() {
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(cal.getTime());
	}

}