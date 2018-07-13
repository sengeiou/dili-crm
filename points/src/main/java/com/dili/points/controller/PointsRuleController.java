package com.dili.points.controller;

import com.dili.points.domain.PointsRule;
import com.dili.points.domain.dto.PointsRuleDTO;
import com.dili.points.provider.FirmProvider;
import com.dili.points.service.PointsRuleService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired FirmProvider firmProvider;


    @ApiOperation("跳转到PointsRule页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index() {
        return "pointsRule/index";
    }

    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String add() {
        return "pointsRule/add";
    }

    @RequestMapping(value = "/get.action", method = RequestMethod.GET)
    @ResponseBody
    public Object get(Long id) {
        PointsRule pointsRule = pointsRuleService.get(id);
        return pointsRule;
    }

    @RequestMapping(value = "/toUpdate.html", method = RequestMethod.GET)
    public String toUpdate(ModelMap modelMap, Long id) {
        PointsRule pointsRule = pointsRuleService.get(id);
        // 启用或者不存在的规则直接跳转到列表页
        if (pointsRule == null || pointsRule.getYn() == 1) {
            return "redirect:/pointsRule/index.html";
        }
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("potins",pointsRule);
        pointsRuleService.buildConditionParameter(modelMap);
        return "pointsRule/update";
    }

    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap, Long id) {
        PointsRule pointsRule = pointsRuleService.get(id);
        if (pointsRule == null) {
            return "redirect:/pointsRule/index.html";
        }
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("potins",pointsRule);
        pointsRuleService.buildConditionParameter(modelMap);
        return "pointsRule/view";
    }

    @RequestMapping(value = "/copy.html", method = RequestMethod.GET)
    public String copy(ModelMap modelMap, Long id) {
        PointsRule pointsRule = pointsRuleService.get(id);
        // 启用或者不存在的规则直接跳转到列表页
        if (pointsRule == null ) {
            return "redirect:/pointsRule/index.html";
        }
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("potins",pointsRule);
        pointsRuleService.buildConditionParameter(modelMap);
        return "pointsRule/copy";
    }

    @ApiOperation(value = "查询PointsRule", notes = "查询PointsRule，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsRule", paramType = "form", value = "PointsRule的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<PointsRule> list(PointsRule pointsRule) {
        return pointsRuleService.list(pointsRule);
    }

    @ApiOperation(value = "分页查询PointsRule", notes = "分页查询PointsRule，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsRule", paramType = "form", value = "PointsRule的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(PointsRuleDTO pointsRule) throws Exception {
    	
    	//设置当前用户所有可以访问的firm
    	pointsRule.setFirmCodes(this.firmProvider.getCurrentUserFirmCodes());
        
    	return pointsRuleService.listEasyuiPageByExample(pointsRule, true).toString();
    }

    @ApiOperation("新增PointsRule")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsRule", paramType = "form", value = "PointsRule的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput insert(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson) {
        pointsRuleService.insertPointRule(pointsRule, numberJson, moneyJson, payMethodJson);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改PointsRule")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsRule", paramType = "form", value = "PointsRule的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(PointsRule pointsRule, String numberJson, String moneyJson, String payMethodJson) {
        pointsRuleService.updatePointRule(pointsRule, numberJson, moneyJson, payMethodJson);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除PointsRule")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "PointsRule的主键", required = true, dataType = "long")
    })
    @RequestMapping(value = "/startPointsRule.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput startPointsRule(PointsRule pointsRule, int status) {
        pointsRuleService.startPointRule(pointsRule, status);
        return BaseOutput.success("成功");
    }

    @RequestMapping(value = "/checkPointsRule.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput checkPointsRule(PointsRule pointsRule) {
        List<PointsRule> ruleList = pointsRuleService.listByExample(pointsRule);
        return CollectionUtils.isNotEmpty(ruleList) ? BaseOutput.failure("已启用相同类型规则 [编码]:" + ruleList.get(0).getCode() + " [名称]:" + ruleList.get(0).getName() + " 请先禁用才能启用此规则!") : BaseOutput.success();
    }

    @RequestMapping(value = "/checkName.action")
    public @ResponseBody
    Object checkName(String name, String org) {
        PointsRuleDTO ex = DTOUtils.newDTO(PointsRuleDTO.class);
        ex.setCheckName(name);
        return checkDuplicate(name, org, ex);
    }

    @RequestMapping(value = "/checkCode.action")
    public @ResponseBody
    Object checkCode(String code, String org) {
        PointsRuleDTO ex = DTOUtils.newDTO(PointsRuleDTO.class);
        ex.setCheckCode(code);
        return checkDuplicate(code, org, ex);
    }
   
    private boolean checkDuplicate(String name, String org, PointsRuleDTO example) {
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(org)) {
            if (name.equals(org)) {
                return true;
            }
        }
        if (StringUtils.isNotBlank(name)) {

            List<PointsRule> ruleList = pointsRuleService.listByExample(example);

            return CollectionUtils.isEmpty(ruleList);
        }
        return false;
    }
}