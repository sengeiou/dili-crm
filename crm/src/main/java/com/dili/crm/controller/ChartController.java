package com.dili.crm.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

	@ApiOperation("跳转到报表页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "chart/index";
	}

	@ApiOperation("跳转到客户报表页面")
	@RequestMapping(value = "/customer.html", method = RequestMethod.GET)
	public String customerChart(ModelMap modelMap) {
		return "chart/customer";
	}
	@ApiOperation("跳转到客户回访报表页面")
	@RequestMapping(value = "/customerVisit.html", method = RequestMethod.GET)
	public String customerVisitChart(ModelMap modelMap) {
		return "chart/customerVisit";
	}
	
	@ApiOperation(value = "查询客户类型分布", notes = "查询Address，返回列表信息")
	@RequestMapping(value = "/customerTypeChart", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerTypeChart() {
		List<CustomerChartDTO>data=this.customerService.selectCustomersGroupByType().getData();
		
		Map<Object, Object> metadata =this.getCustomerMetadata();
		try {
			List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
			for(Map<String,Object>row:list) {
				if(StringUtils.isBlank(String.valueOf(row.get("type")))) {
					row.put("type", "其他");
				}
			}
			return list;
		} catch (Exception e) {
			return Collections.emptyList();
		}
		
	}
	@ApiOperation(value = "查询客户类型分布", notes = "查询客户类型分布，返回客户类型分布信息")
	
	@RequestMapping(value = "/customerMarketChart", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerMarketChart() {
		 List<CustomerChartDTO>data=this.customerService.selectCustomersGroupByMarket().getData();
			Map<Object, Object> metadata =this.getCustomerMetadata();
			try {
				List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
				for(Map<String,Object>row:list) {
					if(StringUtils.isBlank(String.valueOf(row.get("market")))) {
						row.put("makret", "其他");
					}
				}
				return list;
			} catch (Exception e) {
				return Collections.emptyList();
			}
			
	}
	@ApiOperation(value = "查询客户行业分布", notes = "查询客户行业分布，返回客户行业分布信息")
	
	@RequestMapping(value = "/customerProfessionChart", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerProfessionChart() {
		 List<CustomerChartDTO>data=this.customerService.selectCustomersGroupByProfession().getData();
			Map<Object, Object> metadata =this.getCustomerMetadata();
			try {
				List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
				for(Map<String,Object>row:list) {
					if(StringUtils.isBlank(String.valueOf(row.get("profession")))) {
						row.put("profession", "其他");
					}
				}
				return list;
			} catch (Exception e) {
				return Collections.emptyList();
			}
			
	}
	@ApiOperation(value = "查询回访方式分布", notes = "查询回访方式分布，返回回访方式分布信息")
	
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
	
	@ApiOperation(value = "查询回访状态分布", notes = "查询回访状态分布，返回状态分布信息")
	
	
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
	        //回访状态
	        JSONObject visitStateProvider = new JSONObject();
	        visitStateProvider.put("provider", "visitStateProvider");
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
