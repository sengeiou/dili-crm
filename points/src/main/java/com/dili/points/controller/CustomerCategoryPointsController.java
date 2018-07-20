package com.dili.points.controller;

import com.dili.points.domain.CustomerCategoryPoints;
import com.dili.points.domain.dto.CustomerCategoryPointsDTO;
import com.dili.points.service.CustomerCategoryPointsService;
import com.dili.points.service.FirmService;
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

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-05-09 17:35:45.
 */
@Api("/customerCategoryPoints")
@Controller
@RequestMapping("/customerCategoryPoints")
public class CustomerCategoryPointsController {
    @Autowired
    CustomerCategoryPointsService customerCategoryPointsService;
    
    @Autowired
    FirmService firmService;

    @ApiOperation("跳转到CustomerCategoryPoints页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerCategoryPoints/index";
    }

    @ApiOperation(value="查询CustomerCategoryPoints", notes = "查询CustomerCategoryPoints，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerCategoryPoints", paramType="form", value = "CustomerCategoryPoints的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<CustomerCategoryPoints> list(CustomerCategoryPoints customerCategoryPoints) {
        return customerCategoryPointsService.list(customerCategoryPoints);
    }

    @ApiOperation(value="分页查询CustomerCategoryPoints", notes = "分页查询CustomerCategoryPoints，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerCategoryPoints", paramType="form", value = "CustomerCategoryPoints的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(CustomerCategoryPointsDTO customerCategoryPoints) throws Exception {
        return customerCategoryPointsService.listEasyuiPageByExample(customerCategoryPoints, true,this.firmService.getCurrentUserFirmCodes()).toString();
    }

    @ApiOperation("新增CustomerCategoryPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerCategoryPoints", paramType="form", value = "CustomerCategoryPoints的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(CustomerCategoryPoints customerCategoryPoints) {
        customerCategoryPointsService.insertSelective(customerCategoryPoints);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改CustomerCategoryPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerCategoryPoints", paramType="form", value = "CustomerCategoryPoints的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(CustomerCategoryPoints customerCategoryPoints) {
        customerCategoryPointsService.updateSelective(customerCategoryPoints);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除CustomerCategoryPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "CustomerCategoryPoints的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        customerCategoryPointsService.delete(id);
        return BaseOutput.success("删除成功");
    }
}