package com.dili.crm.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.dto.MembersDto;
import com.dili.crm.provider.FirmProvider;
import com.dili.crm.rpc.DepartmentRpc;
import com.dili.crm.rpc.MapRpc;
import com.dili.crm.service.CustomerService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionConstants;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-13 17:11:22.
 * @author asiamaster
 */
@Api("/customer")
@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

	@Autowired
	private ValueProviderUtils valueProviderUtils;
	
	@Autowired
	private DepartmentRpc departmentRpc;

	@Autowired FirmProvider firmProvider;

	@Autowired
	MapRpc mapRpc;
    @ApiOperation("跳转到Customer管理页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
	    UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
	    if(userTicket == null){
		    throw new RuntimeException("未登录");
	    }
		modelMap.put("realName", userTicket.getRealName());
		modelMap.put("departmentId",userTicket.getDepartmentId());
        return "customer/index";
    }

	@ApiOperation("地图打开Customer信息页面")
	@RequestMapping(value="/info.html", method = {RequestMethod.GET, RequestMethod.POST})
	public String info(ModelMap modelMap, @RequestParam(name="id", required = true) Long id) throws Exception {
		customerService.handleDetail(modelMap, id);
		modelMap.put("action", "info");
		return "customer/info";
	}

	@ApiOperation("客户分布页面")
	@RequestMapping(value="/locations.html", method = {RequestMethod.GET, RequestMethod.POST})
	public String locations(ModelMap modelMap, @RequestParam(value = "types",required=false) String[] types, @RequestParam(name="firmCode", required=false) String firmCode)  throws Exception{
    	Map<String,Object> params =  Maps.newHashMap();
		JSONObject queryParams = new JSONObject();
		queryParams.put("dd_code", "customer_type");
		params.put("queryParams", queryParams);
		List<ValuePair<?>> ddList = valueProviderUtils.getLookupList("dataDictionaryValueProvider", null, params);
		//删除  --请选择--
		ddList.remove(0);
		modelMap.put("ddList",ddList);
		Set<String> sets = Sets.newHashSet();
		if(ArrayUtils.isNotEmpty(types)){
			CollectionUtils.addAll(sets,types);
		}else{
			ddList.forEach(d -> {
				sets.add(String.valueOf(d.getValue()));
			});
			sets.add("other");
		}
		modelMap.put("firmCode", firmCode);
		modelMap.put("types",StringUtils.join(sets.toArray(), ";"));
    	modelMap.put("customerAddress", JSONArray.toJSONString(customerService.listCustomerOperating(sets,firmCode)));
		return "customer/locations";
	}

	@ApiOperation("跳转到Customer详情页面")
	@RequestMapping(value="/detail.html", method = {RequestMethod.GET, RequestMethod.POST})
	public String detail(ModelMap modelMap, @RequestParam(name="id", required = true) Long id) throws Exception {
		customerService.handleDetail(modelMap, id);
		modelMap.put("action", "detail");
		return "customer/detail";
	}

	@ApiOperation("跳转到Customer编辑页面")
	@RequestMapping(value="/edit.html", method = {RequestMethod.GET, RequestMethod.POST})
	public String edit(ModelMap modelMap, @RequestParam(name="id", required = true) Long id) throws Exception {
		customerService.handleDetail(modelMap, id);
		modelMap.put("action", "edit");
		return "customer/edit";
	}

	@ApiOperation("跳转到Customer新增页面")
	@RequestMapping(value="/add.html", method = {RequestMethod.GET, RequestMethod.POST})
	public String add(ModelMap modelMap, @RequestParam(name="id", required = false) Long id) throws Exception {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket == null) {
			throw new RuntimeException("未登录");
		}
		if(userTicket.getDepartmentId() == null){
			throw new RuntimeException("请设置当前用户的归属部门，以确认新建客户的归属部门");
		}
		Customer customer = DTOUtils.newDTO(Customer.class);
		customer.setName("temp");
		customer.setYn(0);
		customer.setOwnerId(userTicket.getId());
		customerService.insert(customer);
		modelMap.put("customerId", customer.getId());
		modelMap.put("customer",JSONObject.toJSONString(customer));
		modelMap.put("action", "add");
		//页面上用于展示拥有者和新增时获取拥有者id
		modelMap.put("user", userTicket);
		//页面上用于展示拥有者和新增时获取拥有者id
		modelMap.put("department", departmentRpc.get(userTicket.getDepartmentId()).getData());
		return "customer/edit";
	}

	/**
	 * 展开客户树
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/expand.action", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody String expand(Long parentId) throws Exception {
		return customerService.expandEasyuiPageByParentId(parentId);
	}

    @ApiOperation(value="分页查询Customer", notes = "分页查询Customer，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Customer", paramType="form", value = "Customer的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Customer customer, HttpServletRequest req) throws Exception {
    	//判断导出时全查所有客户
	    String sessionId = req.getHeader(SessionConstants.SESSION_ID);
	    if(StringUtils.isNotBlank(sessionId)){
		    customer.setYn(1);
	    }
		EasyuiPageOutput easyuiPageOutput = customerService.listEasyuiPageByExample(customer, true);
		if(easyuiPageOutput.getTotal() <=0){
			return "{\"rows\":[],\"total\":\"0\"}";
		}
		return easyuiPageOutput.toString();
    }

    @ApiOperation("新增Customer")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Customer", paramType="form", value = "Customer的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Customer customer,Long members[]) {
        return customerService.insertSelectiveWithOutput(customer,members);
    }

    @ApiOperation("修改Customer")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Customer", paramType="form", value = "Customer的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Customer customer, String oldName, String oldMarket) {
		customer.aset("oldName", oldName);
		customer.aset("oldMarket", oldMarket);
	    return customerService.updateSelectiveWithOutput(customer);
    }

    @ApiOperation("删除Customer")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Customer的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
		try {
			return customerService.deleteWithOutput(id);
		} catch (Exception e){
			return BaseOutput.failure("删除失败，系统异常");
		}

    }

    //根据机构类型加载证件类型数据
    @RequestMapping(value="/getCertificateTypeComboboxData.action", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody BaseOutput<String> getCertificateTypeComboboxData(@RequestParam(required = false) String organizationType) throws Exception {
    	Map params = new HashMap(2);
    	params.put("organizationType", organizationType);
		JSONObject queryParam = new JSONObject();
		queryParam.put("required", true);
		params.put(ValueProvider.QUERY_PARAMS_KEY, queryParam);
		String json = JSONArray.toJSONString(valueProviderUtils.getLookupList("certificateTypeProvider", null, params));
		return BaseOutput.success("加载成功").setData(json);
	}

	@RequestMapping(value = "/members.html", method = RequestMethod.GET)
	public String customer(@RequestParam(name="id") Long id, ModelMap modelMap) {
		modelMap.put("customerId", id);
		return "customer/members";
	}

	@ApiOperation(value="分页查询成员客户", notes = "分页查询成员客户，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name="MembersDto", paramType="form", value = "MembersDto的form信息", required = false, dataType = "string")
	})
	@RequestMapping(value="/listMembersPage.action", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody String listMembersPage(MembersDto membersDto) throws Exception {
		return customerService.listMembersPage(membersDto);
	}

	@ApiOperation("删除成员客户")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", paramType="form", value = "Customer的主键", required = true, dataType = "long")
	})
	@RequestMapping(value="/deleteMembers.action", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody BaseOutput deleteMembers(Long id) {
		return customerService.deleteMembers(id);
	}

	@ApiOperation(value="分页查询父客户", notes = "分页查询父客户，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name="MembersDto", paramType="form", value = "MembersDto的form信息", required = false, dataType = "string")
	})
	@RequestMapping(value="/listParentCustomerPage.action", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody String listParentCustomerPage(MembersDto membersDto) throws Exception {
    	if (null == membersDto || null == membersDto.getId()){
    		return customerService.listEasyuiPageByExample(membersDto, true).toString();
		}
		return customerService.listParentCustomerPage(membersDto).toString();
	}

	@RequestMapping(value="/listCustomerPoints.action", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody String listCustomerPoints(@RequestParam(name="customerId") Long customerId) throws Exception {
		return customerService.listCustomerFirmPoints(customerId);
	}

	//获取时间提供者
	private JSONObject getDatetimeProvider(){
		JSONObject datetimeProvider = new JSONObject();
		datetimeProvider.put("provider", "datetimeProvider");
		return datetimeProvider;
	}

}