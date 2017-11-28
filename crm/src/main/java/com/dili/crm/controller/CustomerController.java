package com.dili.crm.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.crm.domain.Customer;
import com.dili.crm.service.CustomerService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import com.google.common.collect.Lists;
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
	public String detail(ModelMap modelMap, @RequestParam(name="id", required = true) Long id) throws Exception {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(userTicket == null){
			throw new RuntimeException("未登录");
		}
		//页面上用于展示拥有者和新增时获取拥有者id
		modelMap.put("user", userTicket);

		Customer customer = customerService.get(id);
		if(customer == null){
			throw new RuntimeException("根据id["+id+"]找不到对应客户");
		}
		List<Map> list = ValueProviderUtils.buildDataByProvider(getCustomerMetadata(), Lists.newArrayList(customer));
		modelMap.put("customer", JSONObject.toJSONString(list.get(0)));
		if(customer.getParentId() != null){
			Customer parent = customerService.get(customer.getParentId());
			modelMap.put("parentCustomer", parent);
		}
		return "customer/detail";
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
		if(id != null){
			Customer parent = customerService.get(id);
			modelMap.put("parentCustomer", parent);
		}
		return "customer/detail";
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

    @ApiOperation(value="查询Customer", notes = "查询Customer，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Customer", paramType="form", value = "Customer的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Customer> list(Customer customer) {
        return customerService.list(customer);
    }

    @ApiOperation(value="分页查询Customer", notes = "分页查询Customer，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Customer", paramType="form", value = "Customer的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Customer customer) throws Exception {
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