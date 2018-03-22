package com.dili.points.controller;

import com.dili.points.domain.PointsRule;
import com.dili.points.service.PointsRuleService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Api("/pointsRule")
@Controller
@RequestMapping("/pointsRule")
public class PointsRuleController {
    @Autowired
    PointsRuleService pointsRuleService;

    @ApiOperation("跳转到PointsRule页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "pointsRule/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "pointsRule/add";
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") Long id) {
        PointsRule pointsRule = pointsRuleService.get(id);
        return pointsRule;
    }

    @RequestMapping(value = "/toUpdate/{id}", method = RequestMethod.GET)
    public String toUpdate(ModelMap modelMap, @PathVariable("id") Long id) {
        modelMap.addAttribute("id", id);
        return "pointsRule/update";
    }

    @ApiOperation(value = "查询PointsRule", notes = "查询PointsRule，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsRule", paramType = "form", value = "PointsRule的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<PointsRule> list(PointsRule pointsRule) {
        return pointsRuleService.list(pointsRule);
    }

    @ApiOperation(value = "分页查询PointsRule", notes = "分页查询PointsRule，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsRule", paramType = "form", value = "PointsRule的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(PointsRule pointsRule) throws Exception {
        return pointsRuleService.listEasyuiPageByExample(pointsRule, true).toString();
    }

    @ApiOperation("新增PointsRule")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsRule", paramType = "form", value = "PointsRule的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput insert(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson) {
        try {
            pointsRuleService.insertPointRule(pointsRule, numberJson, moneyJson, payMethodJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改PointsRule")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsRule", paramType = "form", value = "PointsRule的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson) {
        pointsRuleService.updatePointRule(pointsRule, numberJson, moneyJson, payMethodJson);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除PointsRule")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "PointsRule的主键", required = true, dataType = "long")
    })
    @RequestMapping(value = "/startPointsRule", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput startPointsRule(PointsRule pointsRule) {
        pointsRuleService.startPointRule(pointsRule);
        return BaseOutput.success("删除成功");
    }
}