package com.dili.points.controller;

import com.dili.points.domain.CustomerFirmPoints;
import com.dili.points.service.CustomerFirmPointsService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-07-30 16:20:03.
 */
@Api("/customerFirmPoints")
@Controller
@RequestMapping("/customerFirmPoints")
public class CustomerFirmPointsController {
    @Autowired
    CustomerFirmPointsService customerFirmPointsService;

    @ApiOperation("跳转到CustomerFirmPoints页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerFirmPoints/index";
    }

    @ApiOperation(value="查询CustomerFirmPoints", notes = "查询CustomerFirmPoints，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerFirmPoints", paramType="form", value = "CustomerFirmPoints的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<CustomerFirmPoints> list(CustomerFirmPoints customerFirmPoints) {
        return customerFirmPointsService.list(customerFirmPoints);
    }

    @ApiOperation(value="分页查询CustomerFirmPoints", notes = "分页查询CustomerFirmPoints，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerFirmPoints", paramType="form", value = "CustomerFirmPoints的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(CustomerFirmPoints customerFirmPoints) throws Exception {
        return customerFirmPointsService.listEasyuiPageByExample(customerFirmPoints, true).toString();
    }

    @ApiOperation("新增CustomerFirmPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerFirmPoints", paramType="form", value = "CustomerFirmPoints的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(CustomerFirmPoints customerFirmPoints) {
        customerFirmPointsService.insertSelective(customerFirmPoints);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改CustomerFirmPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerFirmPoints", paramType="form", value = "CustomerFirmPoints的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(CustomerFirmPoints customerFirmPoints) {
        customerFirmPointsService.updateSelective(customerFirmPoints);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除CustomerFirmPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "CustomerFirmPoints的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        customerFirmPointsService.delete(id);
        return BaseOutput.success("删除成功");
    }
}