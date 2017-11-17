package com.dili.crm.controller;

import com.alibaba.fastjson.JSONArray;
import com.dili.crm.domain.Customer;
import com.dili.crm.service.CustomerService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-13 17:11:22.
 */
@Api("/customer")
@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
	@Autowired
	private ValueProviderUtils valueProviderUtils;

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
	public String detail(ModelMap modelMap, @RequestParam(name="id", required = true) Long id) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null){
			throw new RuntimeException("未登录");
		}
		//页面上用于展示拥有者和新增时获取拥有者id
		modelMap.put("user", userTicket);

		Customer customer = customerService.get(id);

		return "customer/detail";
	}

    @ApiOperation(value="查询Customer", notes = "查询Customer，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Customer", paramType="form", value = "Customer的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Customer> list(Customer customer) {
	    customer.setYn(1);
        return customerService.list(customer);
    }

    @ApiOperation(value="分页查询Customer", notes = "分页查询Customer，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Customer", paramType="form", value = "Customer的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Customer customer) throws Exception {
	    customer.setYn(1);
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
	    return customerService.deleteWithOutput(id);
    }

    //根据机构类型加载证件类型数据
    @RequestMapping(value="/getCertificateTypeComboboxData", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody BaseOutput<String> getCertificateTypeComboboxData(@RequestParam(required = false) String organizationType) throws Exception {
    	Map params = new HashMap();
    	params.put("organizationType", organizationType);
		String json = JSONArray.toJSONString(valueProviderUtils.getLookupList("certificateTypeProvider", null, params));
		return BaseOutput.success("加载成功").setData(json);
	}
}