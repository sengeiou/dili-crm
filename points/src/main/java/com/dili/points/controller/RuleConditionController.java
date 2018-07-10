package com.dili.points.controller;

import com.dili.points.domain.RuleCondition;
import com.dili.points.service.RuleConditionService;
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
 * This file was generated on 2018-03-20 11:29:31.
 */
@Api("/ruleCondition")
@Controller
@RequestMapping("/ruleCondition")
public class RuleConditionController {
    @Autowired
    RuleConditionService ruleConditionService;

    @ApiOperation("跳转到RuleCondition页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "ruleCondition/index";
    }

    @ApiOperation(value="查询RuleCondition", notes = "查询RuleCondition，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RuleCondition", paramType="form", value = "RuleCondition的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<RuleCondition> list(RuleCondition ruleCondition) {
        ruleCondition.setOrder("DESC");
        ruleCondition.setSort("modified");
        return ruleConditionService.listByExample(ruleCondition);
    }

    @ApiOperation(value="分页查询RuleCondition", notes = "分页查询RuleCondition，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RuleCondition", paramType="form", value = "RuleCondition的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(RuleCondition ruleCondition) throws Exception {
        return ruleConditionService.listEasyuiPageByExample(ruleCondition, true).toString();
    }

    @ApiOperation("新增RuleCondition")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RuleCondition", paramType="form", value = "RuleCondition的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(RuleCondition ruleCondition) {
        ruleConditionService.insertSelective(ruleCondition);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改RuleCondition")
    @ApiImplicitParams({
		@ApiImplicitParam(name="RuleCondition", paramType="form", value = "RuleCondition的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(RuleCondition ruleCondition) {
        ruleConditionService.updateSelective(ruleCondition);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除RuleCondition")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "RuleCondition的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        ruleConditionService.delete(id);
        return BaseOutput.success("删除成功");
    }
}