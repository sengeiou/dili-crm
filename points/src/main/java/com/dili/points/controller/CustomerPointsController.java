package com.dili.points.controller;

import com.dili.points.domain.Customer;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.service.CustomerPointsService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:30.
 */
@Api("/customerPoints")
@Controller
@RequestMapping("/customerPoints")
public class CustomerPointsController {
    @Autowired
    CustomerPointsService customerPointsService;

    @ApiOperation("跳转到CustomerPoints页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerPoints/index";
    }


    @ApiOperation(value="查询CustomerPoints", notes = "查询CustomerPoints，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerPoints", paramType="form", value = "CustomerPoints的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<CustomerPoints> list(CustomerPoints customerPoints) {
        return customerPointsService.list(customerPoints);
    }

    @ApiOperation(value="分页查询CustomerPoints", notes = "分页查询CustomerPoints，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerPoints", paramType="form", value = "CustomerPoints的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(CustomerApiDTO customer) throws Exception {
    	EasyuiPageOutput easyuiPageOutput = this.customerPointsService.listCustomerPointsByCustomer(customer);
        return easyuiPageOutput.toString();
    }

    @ApiOperation("新增CustomerPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerPoints", paramType="form", value = "CustomerPoints的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(CustomerPoints customerPoints) {
        customerPointsService.insertSelective(customerPoints);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改CustomerPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerPoints", paramType="form", value = "CustomerPoints的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(CustomerPoints customerPoints) {
        customerPointsService.updateSelective(customerPoints);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除CustomerPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "CustomerPoints的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        customerPointsService.delete(id);
        return BaseOutput.success("删除成功");
    }
}