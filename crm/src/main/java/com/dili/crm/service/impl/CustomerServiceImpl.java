package com.dili.crm.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.crm.constant.CrmConstants;
import com.dili.crm.dao.CustomerMapper;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.Department;
import com.dili.crm.domain.dto.*;
import com.dili.crm.rpc.CustomerPointsRpc;
import com.dili.crm.rpc.DepartmentRpc;
import com.dili.crm.rpc.UserRpc;
import com.dili.crm.service.*;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import tk.mybatis.mapper.entity.Example;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
	CustomerStatsService customerStatsService;
    @Autowired
    private CacheService cacheService;
    @Autowired
	ChartService chartService;
	@Autowired
	private DepartmentRpc departmentRpc;
	@Autowired
	private CustomerPointsRpc customerPointsRpc;
	@Autowired
    FirmService firmService;

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
	    //如果不是查询所有市场，并且未传入市场信息，则只能查询当前用户有数据权限的市场的数据
		if (!CrmConstants.ALL_MARKET.equals(customerTreeDto.getMarket()) && (StringUtils.isBlank(customerTreeDto.getMarket()) || CollectionUtils.isEmpty(customerTreeDto.getFirmCodes()))){
			List<String> firmCodes = firmService.getCurrentUserFirmCodes(customerTreeDto.getUserId());
			if (CollectionUtils.isEmpty(firmCodes)){
				return new EasyuiPageOutput(0, null);
			}
			customerTreeDto.setFirmCodes(firmCodes);
		}
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
	@Transactional(rollbackFor = Exception.class)
    public BaseOutput insertSelectiveWithOutput(Customer customer,Long memberIds[]) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("新增失败，登录超时");
        }
        if (checkCertificateNumberExist(customer)) {
            return BaseOutput.failure("证件号码已存在");
        }
        //本系统新增的，写死为crm，对应数据字典
	    customer.setSourceSystem("crm");
        customer.setCreatedId(userTicket.getId());
        customer.setOwnerId(userTicket.getId());
		customer.setYn(1);
        //查询当前用户所属的部门
		Department department = departmentRpc.get(userTicket.getDepartmentId()).getData();
		if (null != department){
			customer.setDepartment(String.valueOf(department.getId()));
		}

        //由于在进入新增页面时，已经预生成了用户，所以，此处直接用修改方法
		super.updateSelective(customer);
		//更改父客户信息
		BaseOutput baseOutput = updateParentIdById(customer.getId(), memberIds);
		return baseOutput.setData(customer);
    }

    @Override
    public BaseOutput updateSelectiveWithOutput(Customer customer) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("修改失败，登录超时");
        }
        if (checkCertificateNumberExist(customer)) {
            return BaseOutput.failure("证件号码已存在");
        }
        customer.setModifiedId(userTicket.getId());
		customer.setModified(new Date());
        boolean hasParent = customer.getParentId() == null;
		customer = DTOUtils.link(customer, get(customer.getId()), Customer.class);
        if(hasParent){
			customer.setParentId(null);
		}
        super.update(customer);
        //如果是修改客户基本信息，需要判断是否修改了市场和客户名称(添加成员客户不进入此条件)
        if(customer.aget("oldMarket") != null && customer.aget("oldName") != null) {
			String oldMarket = customer.aget("oldMarket").toString();
			String oldName = customer.aget("oldName").toString();
			//异步通知积分系统和拉取客户
			Customer finalCustomer = customer;
			new Thread() {
				@Override
				public void run() {
					//修改了客户名称要通知积分系统修改客户品类积分
					if (!StringUtils.equals(oldName, finalCustomer.getName())) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("id", finalCustomer.getId());
						jsonObject.put("name", finalCustomer.getName());
						customerPointsRpc.updateCategoryPoints(jsonObject.toJSONString());
					}
					//修改了市场，要修改客户统计表
					//新市场需要增加客户数，老市场需要减少客户数，但是如果是修改客户数，可能会因为某个时间段没有客户统计，而漏掉，所以客户数需要重新统计
					if (!StringUtils.equals(oldMarket, finalCustomer.getMarket())) {
						customerStatsService.pullCustomerStatsByMarkets(finalCustomer.getCreated(), new Date(), Lists.newArrayList(oldMarket, finalCustomer.getMarket()));
						//			CustomerStats domain = DTOUtils.newDTO(CustomerStats.class);
						//			domain.setDate(DateUtils.formatDate2DateTimeStart(customer.getCreated()));
						//			domain.setCustomerCount(-1);
						//			domain.setFirmCode(oldMarket);
						//			//设置老市场客户数-1
						//			customerStatsService.updateCustomerCount(domain);
						//
						//			domain.setCustomerCount(1);
						//			domain.setFirmCode(customer.getMarket());
						//			//设置新市场客户数+1
						//			customerStatsService.updateCustomerCount(domain);
					}
				}
			}.start();
		}
        return BaseOutput.success("修改成功").setData(customer);
    }

	@Override
	public BaseOutput updateWithOutput(Customer customer) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null){
			return BaseOutput.failure("修改失败，登录超时");
		}
		//证件号码去空格
		customer.setCertificateNumber(customer.getCertificateNumber().trim());
		customer.setModifiedId(userTicket.getId());
		super.update(customer);
		return BaseOutput.success("修改成功").setData(customer);
	}

    @Override
	@Transactional(rollbackFor = Exception.class)
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
        //远程删除客户积分信息
		customerPointsRpc.deleteCustomerPoints(condtion.getId());
        return BaseOutput.success("删除成功");
    }

	@Override
	public BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByType(String firmCode) {
		List<String>firmCodes = this.firmService.getCurrentUserAvaliableFirmCodes(firmCode);
		if(firmCodes.isEmpty()) {
			return new BaseOutput<>().setData(Collections.emptyList());
		}
		return BaseOutput.success().setData(this.getActualDao().selectCustomersGroupByType(firmCodes));
	}

	@Override
	public BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByMarket(String firmCode) {
		List<String>firmCodes = this.firmService.getCurrentUserAvaliableFirmCodes(firmCode);
		if(firmCodes.isEmpty()) {
			return new BaseOutput<>().setData(Collections.emptyList());
		}
		return BaseOutput.<List<CustomerChartDTO>>success().<List<CustomerChartDTO>>setData(this.getActualDao().selectCustomersGroupByMarket(firmCodes));
	}

	@Override
	public BaseOutput<List<CustomerChartDTO>> selectCustomersGroupByProfession(String firmCode) {
		List<String> firmCodes = this.firmService.getCurrentUserAvaliableFirmCodes(firmCode);
		if(firmCodes.isEmpty()) {
			return new BaseOutput<>().setData(Collections.emptyList());
		}
		return BaseOutput.success().setData(this.getActualDao().selectCustomersGroupByProfession(firmCodes));
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
		//界面不显示创建人姓名，暂时停用
//		if(customerMap.get("createdId") != null){
//			BaseOutput<User> userBaseOutput = userRpc.get(Long.parseLong(customerMap.get("createdId").toString()));
//			if(userBaseOutput.isSuccess()) {
//				if(userBaseOutput.getData() == null){
//					customerMap.put("createdName", customerMap.get("createdId"));
//				}else {
//					customerMap.put("createdName", userBaseOutput.getData().getRealName());
//				}
//			}
//		}
		//查询客户可用积分
		CustomerPointsApiDTO customerPointsApiDTO = DTOUtils.newDTO(CustomerPointsApiDTO.class);
		customerPointsApiDTO.setCustomerIds(Lists.newArrayList(customer.getId()));
		BaseOutput<List<CustomerPoints>> output = customerPointsRpc.listCustomerPoints(customerPointsApiDTO);
		if(output.isSuccess() && !output.getData().isEmpty()){
			customerMap.put("available", output.getData().get(0).getAvailable());
			modelMap.put("available", output.getData().get(0).getAvailable());
		}else {
			customerMap.put("available", 0);
			modelMap.put("available", 0);
		}
		modelMap.put("customerId", customerMap.get("id"));
		if (null != customerMap.get("certificateNumber")){
			modelMap.put("certificateNumber", URLEncoder.encode(customerMap.get("certificateNumber").toString(), "utf-8"));
		}
		modelMap.put("customer", JSONObject.toJSONString(customerMap));
	    modelMap.put("startDate",this.calStartDate());
	    modelMap.put("endDate",this.calEndDate());
	    
	    modelMap.put("clientPurchasingTopChartUrl",this.chartService.getClientPurchasingTopChartUrl(this.firmService.getCurrentUserDefaultFirmCode()));
	    modelMap.put("clientSalesTopChartUrl",this.chartService.getClientSalesTopChartUrl(this.firmService.getCurrentUserDefaultFirmCode()));
	    modelMap.put("clientUserContributionChartUrl",this.chartService.getClientUserContributionChartUrl(this.firmService.getCurrentUserDefaultFirmCode()));
		if(customer.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX+"parentId") != null){
			Customer parent = get(Long.parseLong(customer.aget(ValueProviderUtils.ORIGINAL_KEY_PREFIX+"parentId").toString()));
			modelMap.put("parentCustomer", parent);
		}
	}

	@Override
    public String listMembersPage(MembersDto membersDto) throws Exception {
    	//如果传入的当前用户为空，则查询所有
		if (null == membersDto || null == membersDto.getId()) {
			membersDto.setYn(1);
			return listEasyuiPageByExample(membersDto, false).toString();
		}
        String parentIdsStr = getActualDao().getParentCustomers(membersDto.getId());
        if(StringUtils.isBlank(parentIdsStr)) {
        	return null;
        }
        Long id = membersDto.getId();
        List<String> parentIds = Arrays.asList(parentIdsStr.split(","));
	    membersDto.setIdNotIn(parentIds);
        membersDto.setId(null);
		//如果未传入市场信息，则只能查询当前用户有数据权限的市场的数据
		if (StringUtils.isBlank(membersDto.getMarket()) && CollectionUtils.isEmpty(membersDto.getFirmCodes())){
			List<String> firmCodes = firmService.getCurrentUserFirmCodes();
			if (CollectionUtils.isEmpty(firmCodes)){
				return new EasyuiPageOutput(0,null).toString();
			}
			membersDto.setFirmCodes(firmCodes);
		}
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
		//如果未传入市场信息，则只能查询当前用户有数据权限的市场的数据
		if (StringUtils.isBlank(membersDto.getMarket())){
			membersDto.setFirmCodes(firmService.getCurrentUserFirmCodes(null));
		}
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
		metadata.put("certificateType", getDDProvider("certificate_type"));
		metadata.put("organizationType", getDDProvider("customer_organization_type"));
		JSONObject cityProvider = new JSONObject();
		cityProvider.put("provider", "cityProvider");
		metadata.put("operatingArea", cityProvider);
		metadata.put("sourceSystem", getDDProvider("system"));
		metadata.put("market", "firmProvider");
		metadata.put("type", getDDProvider("customer_type"));
		metadata.put("profession", getDDProvider("customer_profession"));

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
	public List<Customer> listCustomerOperating(Set<String> types,String firmCode) {
		List<String> firmCodes = this.firmService.getCurrentUserAvaliableFirmCodes(firmCode);
		if(firmCodes.isEmpty()){
			return Collections.emptyList();
		}

		Example example = new Example(Customer.class);
		Example.Criteria criteria = example.createCriteria();
		/***  构造查询条件 ***/
		//经纬度不能为空
		criteria.andIsNotNull("operatingLng").andIsNotNull("operatingLat");
		//状态为 可用
		criteria.andCondition("yn=1");
		criteria.andIn("market", firmCodes);
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
		return getActualDao().selectByExample(example)
				.parallelStream()
				.map(customer -> {
					DTO dto = DTOUtils.go(customer);
					String name = customer.getName();
					Long id = customer.getId();
					String type = customer.getType();
					String operatingLat = customer.getOperatingLat();
					String operatingLng = customer.getOperatingLng();
					dto.clear();
					customer.setId(id);
					customer.setType(type);
					customer.setOperatingLat(operatingLat);
					customer.setOperatingLng(operatingLng);
					customer.setName(name);
					return customer;
				})
				.collect(Collectors.toList());
	}


	@Override
	public List<Customer> listByExample(CustomerDto domain, List<String> firmCodes) {
		if(firmCodes.isEmpty()) {
			return Collections.emptyList();
		}
		domain.setFirmCodes(firmCodes);
		return this.listByExample(domain);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer deleteWithCascade(List<Long> ids) {
		if (CollectionUtils.isNotEmpty(ids)) {
			return getActualDao().deleteWithCascade(ids);
		}
		return 0;
	}

	@Override
	public List<Customer> listByExample(Customer domain) {
		CustomerTreeDto dto = DTOUtils.as(domain, CustomerTreeDto.class);
		//如果不是查询所有市场，并且未传入市场信息，则只能查询当前用户有数据权限的市场的数据
		if (!CrmConstants.ALL_MARKET.equals(dto.getMarket()) && (StringUtils.isBlank(dto.getMarket()) || CollectionUtils.isEmpty(dto.getFirmCodes()))){
			List<String> firmCodes = firmService.getCurrentUserFirmCodes(dto.getUserId());
			if (CollectionUtils.isEmpty(firmCodes)) {
				return Collections.EMPTY_LIST;
			}
			dto.setFirmCodes(firmCodes);
		}
		if(CrmConstants.ALL_MARKET.equals(dto.getMarket())){
			dto.setMarket(null);
		}
		return super.listByExample(dto);
	}

	@Override
	public List<Customer> listByExample(CustomerDto domain) {
		return super.listByExample(domain);
	}

	@Override
	public String listCustomerFirmPoints(Long customerId){
		List<Map> list = getActualDao().listCustomerFirmPoints(customerId);
		Iterator<Map> it = list.iterator();
		//去掉没积分的数据
		while (it.hasNext()){
			Map map = it.next();
			if(!map.containsKey("available")){
				it.remove();
			}
		}
		Map meta = new HashMap();
		meta.put("tradingFirmCode", "firmProvider");
		try {
			return new EasyuiPageOutput(list.size(), ValueProviderUtils.buildDataByProvider(meta, list)).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
		metadata.put("certificateType", getDDProvider("certificate_type"));
		metadata.put("organizationType", getDDProvider("customer_organization_type"));
		metadata.put("market", getDDProvider("market"));
		metadata.put("type", getDDProvider("customer_type"));
		metadata.put("profession", getDDProvider("customer_profession"));
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
	private JSONObject getDDProvider(String ddCode){
		JSONObject dataDictionaryValueProvider = new JSONObject();
		dataDictionaryValueProvider.put("provider", "dataDictionaryValueProvider");
		dataDictionaryValueProvider.put("queryParams", "{dd_code:\""+ddCode+"\"}");
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

	/**
	 * 批量用户的父客户为某一客户
	 * @param parentId 父客户ID
	 * @param ids 需要更新的客户ID
	 */
	private BaseOutput updateParentIdById(Long parentId,Long ids[]){
		if(null != parentId && null != ids && ids.length > 0){
			Set<Long> memberIds = listMemberIds(parentId);
			for (Long id : ids) {
				 if (!memberIds.contains(id)){
				 	  return BaseOutput.success("成员客户信息异常，成员客户保存失败");
				 }
			}
			Map params = Maps.newHashMap();
			params.put("parentId",parentId);
			params.put("ids",ids);
			getActualDao().updateParentIdById(params);
		}
		return BaseOutput.success("保存成功");

	}

	/**
	 * 根据客户ID查询该客户的可选成员列表的客户ID集
	 * @param id 当前客户ID
	 * @return
	 */
	private Set<Long> listMemberIds(Long id){
		String parentIdsStr = this.getActualDao().getParentCustomers(id);
		if(StringUtils.isBlank(parentIdsStr)) {
			return null;
		}
		List<String> parentIds = Arrays.asList(parentIdsStr.split(","));
		return getActualDao().queryMemberIds(parentIds);
	}

    /**
     * 检查证件号是否存在
     * @param customer
     * @return true-存在
     */
    private boolean checkCertificateNumberExist(Customer customer) {
        if (StringUtils.isNotBlank(customer.getCertificateNumber())) {
            //证件号码去空格
            customer.setCertificateNumber(customer.getCertificateNumber().trim());
            //检查证件号，全局唯一，不能重复
            Customer condition = DTOUtils.newDTO(Customer.class);
            condition.setCertificateNumber(customer.getCertificateNumber());
            condition.setYn(1);
            List<Customer> list = list(condition);
            if (!list.isEmpty()) {
                return true;
            }
        }
        return false;
    }

}