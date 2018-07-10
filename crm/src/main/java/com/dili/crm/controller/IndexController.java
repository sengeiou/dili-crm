package com.dili.crm.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.crm.domain.Customer;
import com.dili.crm.rpc.DataDictionaryRpc;
import com.dili.crm.rpc.SystemConfigRpc;
import com.dili.crm.service.*;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.SystemConfig;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Api
@Controller
public class IndexController {
    @Autowired
    CustomerService customerService;
    @Autowired
    DataDictionaryRpc dataDictionaryRpc;

    @Autowired
    SystemConfigRpc systemConfigRpc;
    @Autowired
    ChartService chartService;
    @Autowired
    private ValueProviderUtils valueProviderUtils;

//    @Autowired
//    private AmqpTemplate amqpTemplate;

    @ApiOperation("跳转到index页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
//        Customer customer = DTOUtils.newDTO(Customer.class);
//        customer.setId(1L);
//        customer.setName("客户1");

//        amqpTemplate.convertAndSend(RabbitConfiguration.DEFAULT_TOPIC_EXCHANGE, RabbitConfiguration.TOPIC_ROUTE_KEY, "客户x");

        int clientRefreshFrequency = this.getRefreshFrequency();
        modelMap.put("startDate", this.calStartDate());
        modelMap.put("endDate", this.calEndDate());
        modelMap.put("indexAbnormalOrdersChartUrl", this.chartService.getIndexAbnormalOrdersChartUrl());
        modelMap.put("indexPurchasingTopChartUrl", this.chartService.getIndexPurchasingTopChartUrl());
        modelMap.put("indexSalesTopChartUrl", this.chartService.getIndexSalesTopChartUrl());
        modelMap.put("chartServer", findChartServer());
        modelMap.put("customerAddress", JSONArray.toJSONString(customerService.listCustomerOperating(null)));
        modelMap.put("clientRefreshFrequency", clientRefreshFrequency);
        Map<String,Object> params =  Maps.newHashMap();
        JSONObject queryParams = new JSONObject();
        queryParams.put("dd_code", "customer_type");
        params.put("queryParams", queryParams);
        List<ValuePair<?>> ddList = valueProviderUtils.getLookupList("dataDictionaryValueProvider", null, params);
        //删除  --请选择--
        ddList.remove(0);
        modelMap.put("ddList",ddList);
        return "index";
    }

    @ApiOperation("跳转到登录页面")
    @RequestMapping(value = "/toLogin.html", method = RequestMethod.GET)
    public String toLogin(ModelMap modelMap) {
        return "toLogin";
    }

    @ApiOperation(value = "分页查询Customer", notes = "分页查询Customer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Customer", paramType = "form", value = "Customer的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listCustomers", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    Object listCustomers(Customer customer) throws Exception {
        customer.setPage(1);
        customer.setRows(10);
        customer.setYn(1);
        customer.setSort("created");
        customer.setOrder("DESC");
        List<Customer> data = this.customerService.listByExample(customer);
        Map<Object, Object> metadata = this.getCustomerMetadata();
        try {
            List<Map> list = ValueProviderUtils.buildDataByProvider(metadata, data);
            return list;
        } catch (Exception e) {
            return Collections.emptyList();
        }

    }

    private Map<Object, Object> getCustomerMetadata() {
        Map<Object, Object> metadata = new HashMap<>();

        metadata.put("market", getDDProvider("market"));
        metadata.put("sourceSystem", getDDProvider("system"));
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

    private String calStartDate() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONDAY, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(cal.getTime());
    }

    private String calEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(cal.getTime());

    }

    private int getRefreshFrequency() {
        int default_frequency = 5;//seconds
        SystemConfig condition = DTOUtils.newDTO(SystemConfig.class);
        condition.setCode("client_refresh_frequency");
        BaseOutput<List<SystemConfig>> list = this.systemConfigRpc.list(condition);

        if (list.isSuccess() && list.getData() != null && list.getData().size() == 1) {
            try {
                return Integer.parseInt(list.getData().get(0).getValue());
            } catch (Exception e) {
                return default_frequency;
            }
        }
        return default_frequency;

    }

    private String findChartServer() {
        String code="thirdparty.servers";
        DataDictionaryValue dataDictionaryValue = DTOUtils.newDTO(DataDictionaryValue.class);
        dataDictionaryValue.setDdCode(code);
        dataDictionaryValue.setCode("chartserver");
        BaseOutput<List<DataDictionaryValue>> output = dataDictionaryRpc.list(dataDictionaryValue);
        if(output.isSuccess() && !output.getData().isEmpty()) {
            return output.getData().get(0).getCode();
        }
        return null;
    }
}
