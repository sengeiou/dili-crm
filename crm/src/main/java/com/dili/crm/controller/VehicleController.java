package com.dili.crm.controller;

import com.dili.crm.domain.Vehicle;
import com.dili.crm.service.VehicleService;
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
 * This file was generated on 2017-11-15 11:16:50.
 */
@Api("/vehicle")
@Controller
@RequestMapping("/vehicle")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @ApiOperation("跳转到Vehicle页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "vehicle/index";
    }

    @ApiOperation(value="查询Vehicle", notes = "查询Vehicle，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Vehicle", paramType="form", value = "Vehicle的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Vehicle> list(Vehicle vehicle) {
        return vehicleService.list(vehicle);
    }

    @ApiOperation(value="分页查询Vehicle", notes = "分页查询Vehicle，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Vehicle", paramType="form", value = "Vehicle的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Vehicle vehicle) throws Exception {
        return vehicleService.listEasyuiPageByExample(vehicle, true).toString();
    }

    @ApiOperation("新增Vehicle")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Vehicle", paramType="form", value = "Vehicle的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Vehicle vehicle) {
        vehicleService.insertSelective(vehicle);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改Vehicle")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Vehicle", paramType="form", value = "Vehicle的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Vehicle vehicle) {
        vehicleService.updateSelective(vehicle);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Vehicle")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Vehicle的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        vehicleService.delete(id);
        return BaseOutput.success("删除成功");
    }
}