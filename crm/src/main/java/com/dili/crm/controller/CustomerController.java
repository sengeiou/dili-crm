package com.dili.crm.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.Department;
import com.dili.crm.domain.dto.MembersDto;
import com.dili.crm.rpc.UserRpc;
import com.dili.crm.service.CustomerService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionConstants;
import com.dili.sysadmin.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
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
	private UserRpc userRpc;

    @ApiOperation("跳转到Customer管理页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
	    UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
	    if(userTicket == null){
		    throw new RuntimeException("未登录");
	    }
	    modelMap.put("realName", userTicket.getRealName());
        return "customer/index";
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
		//页面上用于展示拥有者和新增时获取拥有者id
		modelMap.put("user", userTicket);
		BaseOutput<List<Department>> listBaseOutput = userRpc.listUserDepartmentByUserId(userTicket.getId());
		List<Department> departments = listBaseOutput.getData();
		if (CollectionUtils.isNotEmpty(departments)){
			//页面上用于展示拥有者和新增时获取拥有者id
			modelMap.put("department", departments.get(0));
		}
		return "customer/edit";
	}

	/**
	 * 展开客户树
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/expand", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody String expand(Long parentId) throws Exception {
		return customerService.expandEasyuiPageByParentId(parentId);
	}

    @ApiOperation(value="查询Customer", notes = "查询Customer，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Customer", paramType="form", value = "Customer的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<Customer> list(Customer customer){
        return customerService.list(customer);
    }

    @ApiOperation(value="分页查询Customer", notes = "分页查询Customer，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Customer", paramType="form", value = "Customer的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Customer customer, HttpServletRequest req) throws Exception {
    	//判断导出时全查所有客户
	    String sessionId = req.getHeader(SessionConstants.SESSION_ID);
	    if(StringUtils.isNotBlank(sessionId)){
		    customer.setYn(1);
	    }
	    return customerService.listEasyuiPageByExample(customer, true).toString();
    }

    @ApiOperation("新增Customer")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Customer", paramType="form", value = "Customer的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Customer customer) {
        return customerService.insertSelectiveWithOutput(customer);
    }

    @ApiOperation("修改Customer")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Customer", paramType="form", value = "Customer的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Customer customer) {
	    return customerService.updateSelectiveWithOutput(customer);
    }

    @ApiOperation("删除Customer")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Customer的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
		try {
			return customerService.deleteWithOutput(id);
		} catch (Exception e){
			return BaseOutput.failure("删除失败，系统异常");
		}

    }

    //根据机构类型加载证件类型数据
    @RequestMapping(value="/getCertificateTypeComboboxData", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody BaseOutput<String> getCertificateTypeComboboxData(@RequestParam(required = false) String organizationType) throws Exception {
    	Map params = new HashMap(2);
    	params.put("organizationType", organizationType);
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
	@RequestMapping(value="/listMembersPage", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody String listMembersPage(MembersDto membersDto) throws Exception {
		return customerService.listMembersPage(membersDto).toString();
	}

	@ApiOperation("删除成员客户")
	@ApiImplicitParams({
			@ApiImplicitParam(name="id", paramType="form", value = "Customer的主键", required = true, dataType = "long")
	})
	@RequestMapping(value="/deleteMembers", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody BaseOutput deleteMembers(Long id) {
		return customerService.deleteMembers(id);
	}

	@ApiOperation(value="分页查询父客户", notes = "分页查询父客户，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name="MembersDto", paramType="form", value = "MembersDto的form信息", required = false, dataType = "string")
	})
	@RequestMapping(value="/listParentCustomerPage", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody String listParentCustomerPage(MembersDto membersDto) throws Exception {
    	if (null == membersDto || null == membersDto.getId()){
    		return customerService.listEasyuiPageByExample(membersDto, true).toString();
		}
		return customerService.listParentCustomerPage(membersDto).toString();
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