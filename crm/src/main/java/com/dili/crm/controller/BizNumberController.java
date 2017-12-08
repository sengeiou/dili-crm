package com.dili.crm.controller;

import com.dili.crm.domain.BizNumber;
import com.dili.crm.service.BizNumberService;
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
 * This file was generated on 2017-12-08 09:00:40.
 */
@Api("/bizNumber")
@Controller
@RequestMapping("/bizNumber")
public class BizNumberController {
    @Autowired
    BizNumberService bizNumberService;

    @ApiOperation("跳转到BizNumber页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "bizNumber/index";
    }

    @ApiOperation(value="查询BizNumber", notes = "查询BizNumber，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="BizNumber", paramType="form", value = "BizNumber的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<BizNumber> list(BizNumber bizNumber) {
        return bizNumberService.list(bizNumber);
    }

    @ApiOperation(value="分页查询BizNumber", notes = "分页查询BizNumber，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="BizNumber", paramType="form", value = "BizNumber的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(BizNumber bizNumber) throws Exception {
        return bizNumberService.listEasyuiPageByExample(bizNumber, true).toString();
    }

    @ApiOperation("新增BizNumber")
    @ApiImplicitParams({
		@ApiImplicitParam(name="BizNumber", paramType="form", value = "BizNumber的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(BizNumber bizNumber) {
        bizNumberService.insertSelective(bizNumber);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改BizNumber")
    @ApiImplicitParams({
		@ApiImplicitParam(name="BizNumber", paramType="form", value = "BizNumber的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(BizNumber bizNumber) {
        bizNumberService.updateSelective(bizNumber);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除BizNumber")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "BizNumber的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        bizNumberService.delete(id);
        return BaseOutput.success("删除成功");
    }
}