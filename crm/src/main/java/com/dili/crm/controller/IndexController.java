package com.dili.crm.controller;

import com.dili.crm.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api
@Controller
public class IndexController {
    @Autowired
    CustomerService customerService;
	 @ApiOperation("跳转到index页面")
	    @RequestMapping(value="/index.html", method = RequestMethod.GET)
	    public String index(ModelMap modelMap) {
	        return "index";
	    }
	 
}
