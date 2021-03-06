package com.dili.crm.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.crm.domain.dto.CustomerChartDTO;
import com.dili.crm.domain.dto.CustomerVisitChartDTO;
import com.dili.crm.service.ChartService;
import com.dili.crm.service.CustomerService;
import com.dili.crm.service.CustomerVisitService;
import com.dili.crm.service.FirmService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("/chart")
@Controller
@RequestMapping("/chart")
public class ChartController {
	@Autowired CustomerService customerService;
	@Autowired CustomerVisitService customerVisitService;
	@Autowired ChartService chartService;
	@Autowired
	DataDictionaryRpc dataDictionaryRpc;
	@Autowired
	FirmService firmService;

	@ApiOperation("跳转到报表页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "chart/index";
	}

	private String findChartServer() {
		String code = "thirdparty.servers";
		DataDictionaryValue dataDictionaryValue = DTOUtils.newDTO(DataDictionaryValue.class);
		dataDictionaryValue.setDdCode(code);
		dataDictionaryValue.setCode("chartserver");
		BaseOutput<List<DataDictionaryValue>> output = dataDictionaryRpc.listDataDictionaryValue(dataDictionaryValue);
		if (output.isSuccess() && !output.getData().isEmpty()) {
			return output.getData().get(0).getCode();
		}
		return null;
	}


	private ModelMap addData(ModelMap modelMap,String key,String firmCode) {
		if (StringUtils.isBlank(firmCode)) {
			firmCode = this.firmService.getCurrentUserDefaultFirmCode();
		}
		String url = this.chartService.getChartUrl(key, firmCode);
		modelMap.put("chartServer", findChartServer());
		modelMap.put("firmCode", firmCode);
		modelMap.put("url", url);

		return modelMap;
	}
	@ApiOperation("跳转到销量top(量)报表页面")
	@RequestMapping(value = "/salestopQuantity.html", method = RequestMethod.GET)
	public String salestopQuantityChart(ModelMap modelMap,String firmCode) {
		String key = "SalestopQuantityChartUrl";
		modelMap = this.addData(modelMap, key, firmCode);
		return "chart/report";
	}
	@ApiOperation("跳转到销量top(额)报表页面")
	@RequestMapping(value = "/salestopAmount.html", method = RequestMethod.GET)
	public String salestopAmountChart(ModelMap modelMap,String firmCode) {
		String key = "SalestopAmountChartUrl";
		modelMap = this.addData(modelMap, key, firmCode);
		return "chart/report";
	}
	@ApiOperation("跳转到交易报表(客户)报表页面")
	@RequestMapping(value = "/tradingClient.html", method = RequestMethod.GET)
	public String tradingClientChart(ModelMap modelMap,String firmCode) {
		String key = "TradingClientChartUrl";
		modelMap = this.addData(modelMap, key, firmCode);
		return "chart/report";
	}
	@ApiOperation("跳转到交易报表(客户-产品)报表页面")
	@RequestMapping(value = "/tradingClientProduct.html", method = RequestMethod.GET)
	public String tradingClientProductChart(ModelMap modelMap,String firmCode) {
		String key = "TradingClientProductChartUrl";
		modelMap = this.addData(modelMap, key, firmCode);
		return "chart/report";
	}

	@ApiOperation("跳转到消费top(量)报表页面")
	@RequestMapping(value = "/consumptionQuantity.html", method = RequestMethod.GET)
	public String consumptionQuantityChart(ModelMap modelMap,String firmCode) {
		String key = "ConsumptionQuantityChartUrl";
		modelMap = this.addData(modelMap, key, firmCode);
		return "chart/report";
	}

	@ApiOperation("跳转到消费top(额)报表页面")
	@RequestMapping(value = "/consumptionAmount.html", method = RequestMethod.GET)
	public String consumptionAmountChart(ModelMap modelMap,String firmCode) {
		String key = "ConsumptionAmountChartUrl";
		modelMap = this.addData(modelMap, key, firmCode);
		return "chart/report";
	}

	@ApiOperation("跳转到销售去向报表页面")
	@RequestMapping(value = "/salesarea.html", method = RequestMethod.GET)
	public String salesareaChart(ModelMap modelMap,String firmCode) {
		String key = "SalesareaChartUrl";
		modelMap = this.addData(modelMap, key, firmCode);
		return "chart/report";
	}
	@ApiOperation("跳转到销售去向地区明细报表页面")
	@RequestMapping(value = "/salesareaDetails.html", method = RequestMethod.GET)
	public String salesareaDetailsChart(ModelMap modelMap,String firmCode) {
		String key = "SalesareaDetailsChartUrl";
		modelMap = this.addData(modelMap, key, firmCode);
		return "chart/report";
	}

	@ApiOperation("跳转到销售去向地区+商品明细报表页面")
	@RequestMapping(value = "/salesareaProductDetails.html", method = RequestMethod.GET)
	public String salesareaProductDetailsChart(ModelMap modelMap,String firmCode) {
		String key = "SalesareaProductDetails";
		modelMap = this.addData(modelMap, key, firmCode);
		return "chart/report";
	}

	@ApiOperation("跳转到异常订单报表")
	@RequestMapping(value = "/abnormalOrdersChart.html", method = RequestMethod.GET)
	public String abnormalOrdersChart(ModelMap modelMap,String firmCode) {
		String key = "AbnormalOrdersChartUrl";
		modelMap = this.addData(modelMap, key, firmCode);
		return "chart/report";
	}

	@ApiOperation("跳转到其他报表")
	@RequestMapping(value = "/others/{key}/chart.html", method = RequestMethod.GET)
	public String otherChart(ModelMap modelMap,@PathVariable(value="key")String key,String firmCode) {
		modelMap = this.addData(modelMap, key, firmCode);
		return "chart/report";
	}

	@ApiOperation("跳转到客户报表页面")
	@RequestMapping(value = "/customer.html", method = RequestMethod.GET)
	public String customerChart(ModelMap modelMap,String firmCode) {
		modelMap.put("customerAddress", JSONArray.toJSONString(customerService.listCustomerOperating(null,firmCode)));
		//if(StringUtils.isBlank(firmCode)) {
		//	modelMap.put("firmCode", this.firmProvider.getCurrentUserDefaultFirmCode());
		//}else {
		modelMap.put("firmCode", StringUtils.trimToEmpty(firmCode));
		//}

		return "chart/customer";
	}
	@ApiOperation("跳转到客户回访报表页面")
	@RequestMapping(value = "/customerVisit.html", method = RequestMethod.GET)
	public String customerVisitChart(ModelMap modelMap,String firmCode) {
		modelMap.put("firmCode", StringUtils.trimToEmpty(firmCode));
		return "chart/customerVisit";
	}

	@ApiOperation(value = "查询客户类型分布", notes = "查询Address，返回列表信息")
	@RequestMapping(value = "/customerTypeChart.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerTypeChart(String firmCode) {
		List<CustomerChartDTO>data = this.customerService.selectCustomersGroupByType(firmCode).getData();
		Map<Object, Object> metadata = this.getCustomerMetadata();
		try {
			List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
			return this.addOthers(list, "type");
		} catch (Exception e) {
			return Collections.emptyList();
		}

	}
	@ApiOperation(value = "查询客户市场分布", notes = "查询客户市场分布")

	@RequestMapping(value = "/customerMarketChart.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerMarketChart(String firmCode) {
		List<CustomerChartDTO> data = this.customerService.selectCustomersGroupByMarket(firmCode).getData();
		Map<Object, Object> metadata = this.getCustomerMetadata();
		try {
			List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
			return this.addOthers(list, "market");
		} catch (Exception e) {
			return Collections.emptyList();
		}

	}
	@ApiOperation(value = "查询客户行业分布", notes = "查询客户行业分布，返回客户行业分布信息")

	@RequestMapping(value = "/customerProfessionChart.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerProfessionChart(String firmCode) {
		List<CustomerChartDTO> data = this.customerService.selectCustomersGroupByProfession(firmCode).getData();
		Map<Object, Object> metadata = this.getCustomerMetadata();
		try {
			List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
			return this.addOthers(list, "profession");
		} catch (Exception e) {
			return Collections.emptyList();
		}

	}
	@ApiOperation(value = "查询回访方式分布", notes = "查询回访方式分布，返回回访方式分布信息")

	@RequestMapping(value = "/customerVisitModeChart.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerVisitModeChart(String firmCode) {
		List<CustomerVisitChartDTO> data = this.customerVisitService.selectCustomerVisitGroupByMode(firmCode).getData();
		Map<Object, Object> metadata = this.getCustomerVisitMetadata();
		try {
			List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
			return list;
		} catch (Exception e) {
			return Collections.emptyList();
		}

	}

	@ApiOperation(value = "查询回访状态分布", notes = "查询回访状态分布，返回状态分布信息")


	@RequestMapping(value = "/customerVisitStateChart.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Object customerVisitStateChart(String firmCode) {
		List<CustomerVisitChartDTO> data = this.customerVisitService.selectCustomerVisitGroupByState(firmCode).getData();
		Map<Object, Object> metadata = this.getCustomerVisitMetadata();
		try {
			List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
			return list;
		} catch (Exception e) {
			return Collections.emptyList();
		}

	}
	private List<Map>addOthers(List<Map> list,String key){
		for(Map<String,Object>row:list) {
			if(StringUtils.isBlank(String.valueOf(row.get(key)))) {
				row.put(key, "其他");
			}
		}
		return list;
	}
	private Map<Object, Object> getCustomerVisitMetadata(){
		Map<Object, Object> metadata = new HashMap<>();
		//回访状态
		//回访状态
		JSONObject visitStateProvider = new JSONObject();
		visitStateProvider.put("provider", "visitStateProvider");
		metadata.put("state", visitStateProvider);
		//回访方式
		metadata.put("mode", getDDProvider("visit_mode"));
		return metadata;
	}


	private Map<Object, Object> getCustomerMetadata(){
		Map<Object, Object> metadata = new HashMap<>();

		metadata.put("market", "firmProvider");
		metadata.put("type", getDDProvider("customer_type"));
		metadata.put("profession", getDDProvider("customer_profession"));
		return metadata;
	}
	//获取数据字典提供者
	private JSONObject getDDProvider(String ddCode){
		JSONObject dataDictionaryValueProvider = new JSONObject();
		dataDictionaryValueProvider.put("provider", "dataDictionaryValueProvider");
		dataDictionaryValueProvider.put("queryParams", "{dd_code:\""+ddCode+"\"}");
		return dataDictionaryValueProvider;
	}
}
