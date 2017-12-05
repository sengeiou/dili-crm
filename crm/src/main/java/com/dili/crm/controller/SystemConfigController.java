package com.dili.crm.controller;

import com.dili.crm.domain.SystemConfig;
import com.dili.crm.service.SystemConfigService;
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
 * This file was generated on 2017-12-05 14:23:24.
 */
@Api("/systemConfig")
@Controller
@RequestMapping("/systemConfig")
public class SystemConfigController {
    @Autowired
    SystemConfigService systemConfigService;

    @ApiOperation("跳转到SystemConfig页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "systemConfig/index";
    }

    @ApiOperation(value="查询SystemConfig", notes = "查询SystemConfig，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="SystemConfig", paramType="form", value = "SystemConfig的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<SystemConfig> list(SystemConfig systemConfig) {
        return systemConfigService.list(systemConfig);
    }

    @ApiOperation(value="分页查询SystemConfig", notes = "分页查询SystemConfig，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="SystemConfig", paramType="form", value = "SystemConfig的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(SystemConfig systemConfig) throws Exception {
        return systemConfigService.listEasyuiPageByExample(systemConfig, true).toString();
    }

    @ApiOperation("新增SystemConfig")
    @ApiImplicitParams({
		@ApiImplicitParam(name="SystemConfig", paramType="form", value = "SystemConfig的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(SystemConfig systemConfig) {
	    systemConfig.setYn(1);
        systemConfigService.insertSelective(systemConfig);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改SystemConfig")
    @ApiImplicitParams({
		@ApiImplicitParam(name="SystemConfig", paramType="form", value = "SystemConfig的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(SystemConfig systemConfig) {
        systemConfigService.updateSelective(systemConfig);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除SystemConfig")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "SystemConfig的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        systemConfigService.delete(id);
        return BaseOutput.success("删除成功");
    }
}