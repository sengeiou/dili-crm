package com.dili.crm.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.crm.service.ICardETLService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.AESUtil;
import com.dili.ss.util.DateUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@RequestMapping("/adminManagement")
public class AdminManagementController {
    private static final AtomicBoolean ETLISRUNNING = new AtomicBoolean(false);//同步客户数据的服务是否正在执行
    private static final Logger LOG = LoggerFactory.getLogger(AdminManagementController.class);
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    ICardETLService icardETLService;

    @Value("${aesKey:}")
    private String aesKey;

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "adminManagement/index";
    }

    @RequestMapping(value = "/sendJsonToMQ.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput sendJsonToMQ(String type, String json) throws Exception {
        try {
            json = AESUtil.encrypt(json, aesKey);
            if ("customer".equals(type)) {
                amqpTemplate.convertAndSend("diligrp.crm.topicExchange", "diligrp.crm.addCustomerKey", json);
            } else if ("order".equals(type)) {
                amqpTemplate.convertAndSend("diligrp.points.topicExchange", "diligrp.points.syncOrderKey", json);
            } else if ("category".equals(type)) {
                amqpTemplate.convertAndSend("diligrp.points.topicExchange", "diligrp.points.syncCategoryKey", json);
            } else {
                return BaseOutput.failure("数据类型错误");
            }
        } catch (Exception e) {
            return BaseOutput.failure("json格式错误");
        }
        return BaseOutput.success("发送成功");
    }

    @RequestMapping(value = "/sendJsonToMQTest.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput sendJsonToMQTest() throws Exception {
        try {

            JSONObject jo = new JSONObject();
            JSONObject data = new JSONObject();
            jo.put("data", data);
            jo.put("type", "json");
            jo.put("from", "crm");
            jo.put("sendTime", DateUtils.format(new Date()));
            data.put("certificateType", "id");
            data.put("organizationType", "individuals");
            data.put("type", "sale");
            data.put("sex", "male");
            data.put("sourceSystem", "crm");
            data.put("market", "hd");
            data.put("created", "2018-09-11 09:36:00");
            data.put("notes", "第一次性能测试");
            //地址
            String[] addresses = {"测试测试客户地址1", "测试测试客户地址2"};
            data.put("address", addresses);
            //扩展信息
            JSONArray ja = new JSONArray();
            JSONObject ext = new JSONObject();
            ext.put("acct", "1001");
            ext.put("notes", "卡号:12345678");
            ext.put("acctType", "masterCard");
            ja.add(ext);
            JSONObject ext2 = new JSONObject();
            ext2.put("acct", "1002");
            ext2.put("notes", "卡号:23456789");
            ext2.put("acctType", "slaveCard");
            ja.add(ext);
            data.put("extensions", ja);
            for(int i=0; i<1; i++){
                data.put("name", "性能测试客户-"+i);
                data.put("certificateNumber", "51010519770101"+String.format("%04d", i));
                data.put("phone", "130" + String.format("%08d", i));
                amqpTemplate.convertAndSend("diligrp.crm.topicExchange", "diligrp.crm.addCustomerKey", AESUtil.encrypt(jo.toJSONString(), aesKey));
//                amqpTemplate.convertAndSend("diligrp.crm.topicExchange", "diligrp.crm.addCustomerKey", jo.toJSONString());
            }
        } catch (Exception e) {
            return BaseOutput.failure("json格式错误");
        }

        return BaseOutput.success("发送成功");
    }

    @RequestMapping(value = "/loadCustomers.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput loadCustomers() throws Exception {

        try {
            if (ETLISRUNNING.compareAndSet(false, true)) {
                Thread t = new Thread(() -> {

                    try {
                        while (true) {
                            boolean value = icardETLService.transIncrementData(null, 100);
                            if (!value) {
                                break;
                            }
                        }
                    } catch (Exception e) {
                        LOG.error("手动触发客户信息同步出错:" + e.getMessage(), e);
                    }
                    ETLISRUNNING.set(false);
                });
                t.start();

            } else {
                return BaseOutput.success("客户信息已经在同步,请稍后重试");
            }


        } catch (Exception e) {
            return BaseOutput.failure("程序错误");
        }


        return BaseOutput.success("同步客户信息任务已经启动");
    }

}
