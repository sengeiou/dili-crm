package com.dili.crm.controller;

import com.dili.crm.domain.CustomerExtensions;
import com.dili.crm.service.CustomerExtensionsService;
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
 * This file was generated on 2017-11-28 14:51:20.
 */
@Api("/customerExtensions")
@Controller
@RequestMapping("/customerExtensions")
public class CustomerExtensionsController {
    @Autowired
    CustomerExtensionsService customerExtensionsService;

    @ApiOperation("跳转到CustomerExtensions页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerExtensions/index";
    }

    @ApiOperation(value="查询CustomerExtensions", notes = "查询CustomerExtensions，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerExtensions", paramType="form", value = "CustomerExtensions的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<CustomerExtensions> list(CustomerExtensions customerExtensions) {
        return customerExtensionsService.list(customerExtensions);
    }

    @ApiOperation(value="分页查询CustomerExtensions", notes = "分页查询CustomerExtensions，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerExtensions", paramType="form", value = "CustomerExtensions的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(CustomerExtensions customerExtensions) throws Exception {
        return customerExtensionsService.listEasyuiPageByExample(customerExtensions, true).toString();
    }

    @ApiOperation("新增CustomerExtensions")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerExtensions", paramType="form", value = "CustomerExtensions的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(CustomerExtensions customerExtensions) {
        customerExtensionsService.insertSelective(customerExtensions);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改CustomerExtensions")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerExtensions", paramType="form", value = "CustomerExtensions的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(CustomerExtensions customerExtensions) {
        customerExtensionsService.updateSelective(customerExtensions);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除CustomerExtensions")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "CustomerExtensions的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        customerExtensionsService.delete(id);
        return BaseOutput.success("删除成功");
    }
}