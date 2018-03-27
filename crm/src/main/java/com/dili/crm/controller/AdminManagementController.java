package com.dili.crm.controller;

import java.util.HashMap;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.crm.domain.BizNumber;
import com.dili.crm.service.ICardETLService;
import com.dili.ss.domain.BaseOutput;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/adminManagement")
public class AdminManagementController {
	
	 @Autowired AmqpTemplate amqpTemplate;
	 @Autowired ICardETLService icardETLService;
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "adminManagement/index";
    }
    @RequestMapping(value="/sendJsonToMQ", method = {RequestMethod.GET, RequestMethod.POST})
    public  @ResponseBody BaseOutput  sendJsonToMQ(String type,String json) throws Exception {
    	
    	try {
    		ObjectMapper objMapper=new ObjectMapper();
    		HashMap<String, Object> map = objMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {});
    		
    	}catch(Exception e) {
    		return BaseOutput.failure("json格式错误");
    	}
    	if("customer".equals(type)) {
    		amqpTemplate.convertAndSend("diligrp.crm.topicExchange", "diligrp.crm.addCustomerKey", json);
    	}else if("order".equals(type)) {
    		amqpTemplate.convertAndSend("diligrp.points.topicExchange", "diligrp.points.syncOrderKey", json);
    	}else if("category".equals(type)) {
    		amqpTemplate.convertAndSend("diligrp.points.topicExchange", "diligrp.points.syncCategoryKey", json);
    	}else {
    		return BaseOutput.failure("数据类型错误");
    	}
    	
        return BaseOutput.success("发送成功");
    }
    @RequestMapping(value="/loadCustomers", method = {RequestMethod.GET, RequestMethod.POST})
    public  @ResponseBody BaseOutput  loadCustomers() throws Exception {
    	
    	try {
    		Thread t=new Thread(()->{
    			while(true) {
        			boolean value=icardETLService.transIncrementData(null, 100);
        			if(!value) {
        				break;
        			}
        		}
    		});
    		t.start();
    	
    	}catch(Exception e) {
    		return BaseOutput.failure("程序错误");
    	}
    	
    	
        return BaseOutput.success("正在同步");
    }
    
}
