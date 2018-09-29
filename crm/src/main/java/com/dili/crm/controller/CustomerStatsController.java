package com.dili.crm.controller;

import com.dili.crm.domain.dto.CustomerStatsDto;
import com.dili.crm.service.CustomerStatsService;
import com.dili.ss.domain.BaseOutput;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-09-26 11:58:22.
 */
@Api("/customerStats")
@Controller
@RequestMapping("/customerStats")
public class CustomerStatsController {
    @Autowired
    CustomerStatsService customerStatsService;

    @ApiOperation("跳转到CustomerStats页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerStats/index";
    }

    @ApiOperation(value="查询CustomerStats", notes = "查询CustomerStats，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerStats", paramType="form", value = "CustomerStats的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput<List<Map>> list(CustomerStatsDto customerStats) throws Exception {
        //默认查一个月
        initStartDate(customerStats);
        return customerStatsService.listCustomerStats(customerStats);
    }

    @ApiOperation(value="查询各市场客户增量", notes = "查询各市场客户增量，返回饼图信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="CustomerStats", paramType="form", value = "CustomerStats的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value="/listIncrement.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput<List<Map>> listIncrement(CustomerStatsDto customerStats) throws Exception {
        //默认查一个月
        initStartDate(customerStats);
        return customerStatsService.listCustomerStatsIncrement(customerStats);
    }

    //初始化开始时间，默认查一个月
    private void initStartDate(CustomerStatsDto customerStats){
        if("true".equals(customerStats.aget("init"))){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            customerStats.setStartDate(calendar.getTime());
        }
    }

}