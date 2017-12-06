package com.dili.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dili.crm.domain.SystemConfig;
import com.dili.crm.service.CustomerService;
import com.dili.crm.service.DataDictionaryService;
import com.dili.crm.service.DataDictionaryValueService;
import com.dili.crm.service.SystemConfigService;
import com.dili.ss.dto.DTOUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@Controller
public class IndexController {
    @Autowired CustomerService customerService;
    @Autowired DataDictionaryService dataDictionaryService;
    @Autowired DataDictionaryValueService dataDictionaryValueService;
    @Autowired SystemConfigService systemConfigService;
    
	 @ApiOperation("跳转到index页面")
	    @RequestMapping(value="/index.html", method = RequestMethod.GET)
	    public String index(ModelMap modelMap) {
			 int clientRefreshFrequency=this.getRefreshFrequency();
			 modelMap.put("clientRefreshFrequency", clientRefreshFrequency);
	        return "index";
	    }
	 private int getRefreshFrequency() {
		 int default_frequency=5;//seconds
		 
		 SystemConfig condition=DTOUtils.newDTO(SystemConfig.class);
		 condition.setCode("client_refresh_frequency");
		 condition.setYn(1);
		 List<SystemConfig>list=this.systemConfigService.list(condition);
		 
		 if(list!=null&&list.size()==1) {
			 try {
				 return Integer.parseInt(list.get(0).getValue());
			 }catch(Exception e) {
				 return default_frequency;
			 }
		 }
		 return default_frequency;
		 
	 }
	 
}
