package com.dili.crm.controller;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.DataDictionaryValue;
import com.dili.crm.domain.dto.DataDictionaryDto;
import com.dili.crm.service.CustomerService;
import com.dili.crm.service.DataDictionaryService;
import com.dili.crm.service.DataDictionaryValueService;
import com.dili.ss.dto.DTOUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api
@Controller
public class IndexController {
    @Autowired CustomerService customerService;
    @Autowired DataDictionaryService dataDictionaryService;
    @Autowired DataDictionaryValueService dataDictionaryValueService;
	 @ApiOperation("跳转到index页面")
	    @RequestMapping(value="/index.html", method = RequestMethod.GET)
	    public String index(ModelMap modelMap) {
			 int clientRefreshFrequency=this.getRefreshFrequency();
			 modelMap.put("clientRefreshFrequency", clientRefreshFrequency);
	        return "index";
	    }
	 private int getRefreshFrequency() {
		 int default_frequency=6;//seconds
		 DataDictionaryDto dataDictionaryDto=this.dataDictionaryService.findByCode("index_refresh_frequence");
		 if(dataDictionaryDto==null) {
			 return default_frequency;
		 }
		 DataDictionaryValue condtion=DTOUtils.newDTO(DataDictionaryValue.class);
		 condtion.setDdId(dataDictionaryDto.getId());
		 condtion.setYn(1);
		 condtion.setCode("client_refresh_frequency");
		 List<DataDictionaryValue>list=this.dataDictionaryValueService.list(condtion);
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
