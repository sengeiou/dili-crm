package com.dili.crm.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dili.crm.domain.Address;
import com.dili.crm.domain.dto.CustomerChartDTO;
import com.dili.crm.domain.dto.CustomerVisitChartDTO;
import com.dili.crm.service.CustomerService;
import com.dili.crm.service.CustomerVisitService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api("/chart")
@Controller
@RequestMapping("/chart")
public class ChartController {
	@Autowired
	CustomerService customerService;
	@Autowired CustomerVisitService customerVisitService;

	@ApiOperation("跳转到图表页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "chart/index";
	}

	@ApiOperation("跳转到图表页面")
	@RequestMapping(value = "/customer.html", method = RequestMethod.GET)
	public String customerChart(ModelMap modelMap) {
		return "chart/customer";
	}
	@ApiOperation("跳转到图表页面")
	@RequestMapping(value = "/customerVisit.html", method = RequestMethod.GET)
	public String customerVisitChart(ModelMap modelMap) {
		return "chart/customerVisit";
	}
	@ApiOperation(value = "查询Address", notes = "查询Address，返回列表信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Address", paramType = "form", value = "Address的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/customerTypeChart", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerTypeChart() {
		List<CustomerChartDTO>data=this.customerService.selectCustomersGroupByType().getData();
		
		Map<Object, Object> metadata =this.getCustomerMetadata();
		try {
			List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
			return list;
		} catch (Exception e) {
			return Collections.emptyList();
		}
		
	}

	@RequestMapping(value = "/customerMarketChart", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerMarketChart() {
		 List<CustomerChartDTO>data=this.customerService.selectCustomersGroupByMarket().getData();
			Map<Object, Object> metadata =this.getCustomerMetadata();
			try {
				List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
				return list;
			} catch (Exception e) {
				return Collections.emptyList();
			}
			
	}

	@RequestMapping(value = "/customerProfessionChart", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerProfessionChart() {
		 List<CustomerChartDTO>data=this.customerService.selectCustomersGroupByProfession().getData();
			Map<Object, Object> metadata =this.getCustomerMetadata();
			try {
				List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
				return list;
			} catch (Exception e) {
				return Collections.emptyList();
			}
			
	}
	
	@RequestMapping(value = "/customerVisitModeChart", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerVisitModeChart() {
		 List<CustomerVisitChartDTO>data=this.customerVisitService.selectCustomerVisitGroupByMode().getData();
			Map<Object, Object> metadata =this.getCustomerVisitMetadata();
			try {
				List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
				return list;
			} catch (Exception e) {
				return Collections.emptyList();
			}
			
	}
	
	
	@RequestMapping(value = "/customerVisitStateChart", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerVisitStateChart() {
		 	List<CustomerVisitChartDTO>data=this.customerVisitService.selectCustomerVisitGroupByState().getData();
			Map<Object, Object> metadata =this.getCustomerVisitMetadata();
			try {
				List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
				return list;
			} catch (Exception e) {
				return Collections.emptyList();
			}
			
	}
	
	 private Map<Object, Object> getCustomerVisitMetadata(){
	        Map<Object, Object> metadata = new HashMap<>();
	        //回访状态
	        JSONObject visitStateProvider = new JSONObject();
	        metadata.put("state", visitStateProvider);
	        //回访方式
	        metadata.put("mode", getDDProvider(11L));
	        return metadata;
	    }

	 
	private Map<Object, Object> getCustomerMetadata(){
		Map<Object, Object> metadata = new HashMap<>();

		metadata.put("market", getDDProvider(2L));
		metadata.put("type", getDDProvider(4L));
		metadata.put("profession", getDDProvider(6L));
		return metadata;
	}
	//获取数据字典提供者
	private JSONObject getDDProvider(Long ddId){
		JSONObject dataDictionaryValueProvider = new JSONObject();
		dataDictionaryValueProvider.put("provider", "dataDictionaryValueProvider");
		dataDictionaryValueProvider.put("queryParams", "{dd_id:"+ddId+"}");
		return dataDictionaryValueProvider;
	}
}
