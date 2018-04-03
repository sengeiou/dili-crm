package com.dili.crm.controller;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dili.crm.domain.BizNumber;
import com.dili.crm.service.ICardETLService;
import com.dili.ss.domain.BaseOutput;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/adminManagement")
public class AdminManagementController {
	private static final AtomicBoolean ETLISRUNNING=new AtomicBoolean(false);//同步客户数据的服务是否正在执行
	private static final Logger LOG=LoggerFactory.getLogger(AdminManagementController.class);
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
        	if("customer".equals(type)) {
        		amqpTemplate.convertAndSend("diligrp.crm.topicExchange", "diligrp.crm.addCustomerKey", map);
        	}else if("order".equals(type)) {
        		amqpTemplate.convertAndSend("diligrp.points.topicExchange", "diligrp.points.syncOrderKey", map);
        	}else if("category".equals(type)) {
        		amqpTemplate.convertAndSend("diligrp.points.topicExchange", "diligrp.points.syncCategoryKey", map);
        	}else {
        		return BaseOutput.failure("数据类型错误");
        	}
        	
    	}catch(Exception e) {
    		return BaseOutput.failure("json格式错误");
    	}
    	
        return BaseOutput.success("发送成功");
    }
    @RequestMapping(value="/loadCustomers", method = {RequestMethod.GET, RequestMethod.POST})
    public  @ResponseBody BaseOutput  loadCustomers() throws Exception {
    	
    	try {
    		if(ETLISRUNNING.compareAndSet(false, true)) {
        		Thread t=new Thread(()->{
        			
        			try {
        				while(true) {
        					boolean value=icardETLService.transIncrementData(null, 100);
                			if(!value) {
                				break;
                			}	
                		}
    				}catch(Exception e) {
    					LOG.error("手动触发客户信息同步出错:"+e.getMessage(),e);
    				}
        			ETLISRUNNING.set(false);
        		});
        		t.start();
    			
    		}else {
    			return BaseOutput.success("客户信息已经在同步,请稍后重试");		
    		}

    	
    	}catch(Exception e) {
    		return BaseOutput.failure("程序错误");
    	}
    	
    	
        return BaseOutput.success("同步客户信息任务已经启动");
    }
    
}
