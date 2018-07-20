package com.dili.points.controller;

import com.dili.points.domain.PointsRule;
import com.dili.points.domain.dto.PointsRuleDTO;
import com.dili.points.service.FirmService;
import com.dili.points.service.PointsRuleService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Firm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 * @author wangguofeng
 */
@Api("/pointsRule")
@Controller
@RequestMapping("/pointsRule")
public class PointsRuleController {
    @Autowired
    PointsRuleService pointsRuleService;
    @Autowired
    FirmService firmService;


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
    	return pointsRuleService.listEasyuiPageByExample(pointsRule, true,this.firmService.getCurrentUserFirmCodes()).toString();
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
	public @ResponseBody BaseOutput checkPointsRule(PointsRule pointsRule) {
		List<PointsRule> ruleList = pointsRuleService.listByExample(pointsRule);
		if (CollectionUtils.isNotEmpty(ruleList)) {
			PointsRule pointsRuleItem = ruleList.get(0);
			String firmCode = pointsRuleItem.getFirmCode();
			//查询市场名称
			Optional<Firm> opt = this.firmService.getFirmByCode(firmCode);
			String firmName = opt.map(Firm::getName).orElse(firmCode);
			String failMsg = "已启用相同类型规则  [市场]:" + firmName 
					+ " [编码]:" + pointsRuleItem.getCode() 
					+ " [名称]:"+ pointsRuleItem.getName()
					+ " 请先禁用才能启用此规则!";
			return BaseOutput.failure(failMsg);
		}
		return BaseOutput.success();
	}

//    @RequestMapping(value = "/checkName.action")
//    public @ResponseBody
//    Object checkName(String name,String firmCode, String org) {
//        PointsRuleDTO ex = DTOUtils.newDTO(PointsRuleDTO.class);
//        ex.setCheckName(name);
//        ex.setFirmCode(firmCode);
//        return checkDuplicate(name, org, ex);
//    }
//
//    @RequestMapping(value = "/checkCode.action")
//    public @ResponseBody
//    Object checkCode(String code,String firmCode, String org) {
//        PointsRuleDTO ex = DTOUtils.newDTO(PointsRuleDTO.class);
//        ex.setCheckCode(code);
//        ex.setFirmCode(firmCode);
//        return checkDuplicate(code, org, ex);
//    }
    @RequestMapping(value = "/checkPointsRuleCodeAndName.action")
	public @ResponseBody boolean checkPointsRuleCodeAndName(String firmCode, String code, String name, Long id) {

		List<PointsRule> ruleList = new ArrayList<>();
		// 查询出所有在同一市场code相同的规则
		if (StringUtils.isNotBlank(code)) {
			PointsRuleDTO condition = DTOUtils.newDTO(PointsRuleDTO.class);
			condition.setFirmCode(firmCode);
			condition.setCheckCode(code);
			ruleList.addAll(pointsRuleService.listByExample(condition));
		}
		// 查询出所有在同一市场name相同的规则
		if (StringUtils.isNotBlank(name)) {
			PointsRuleDTO condition = DTOUtils.newDTO(PointsRuleDTO.class);
			condition.setFirmCode(firmCode);
			condition.setCheckName(name);
			ruleList.addAll(pointsRuleService.listByExample(condition));
		}
		// id转换为集合并去重
		List<Long> ids = ruleList.stream().map(PointsRule::getId).distinct().collect(Collectors.toList());
		// 更新
		if (id != null) {
			return ids.size() == 1 && ids.contains(id);
		} else {
			// 规则插入
			return CollectionUtils.isEmpty(ids);
		}
	}
//    private boolean checkDuplicate(String name, String org, PointsRuleDTO example) {
//        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(org)) {
//            if (name.equals(org)) {
//                return true;
//            }
//        }
//        if (StringUtils.isNotBlank(name)) {
//
//            List<PointsRule> ruleList = pointsRuleService.listByExample(example);
//
//            return CollectionUtils.isEmpty(ruleList);
//        }
//        return false;
//    }
}