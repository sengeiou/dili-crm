package com.dili.points.controller;

import com.dili.points.domain.PointsRuleLog;
import com.dili.points.service.PointsRuleLogService;
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
 * This file was generated on 2018-03-20 11:29:31.
 */
@Api("/pointsRuleLog")
@Controller
@RequestMapping("/pointsRuleLog")
public class PointsRuleLogController {
    @Autowired
    PointsRuleLogService pointsRuleLogService;

    @ApiOperation("跳转到PointsRuleLog页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "pointsRuleLog/index";
    }

    @ApiOperation(value="查询PointsRuleLog", notes = "查询PointsRuleLog，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsRuleLog", paramType="form", value = "PointsRuleLog的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<PointsRuleLog> list(PointsRuleLog pointsRuleLog) {
        return pointsRuleLogService.list(pointsRuleLog);
    }

    @ApiOperation(value="分页查询PointsRuleLog", notes = "分页查询PointsRuleLog，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsRuleLog", paramType="form", value = "PointsRuleLog的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(PointsRuleLog pointsRuleLog) throws Exception {
        return pointsRuleLogService.listEasyuiPageByExample(pointsRuleLog, true).toString();
    }

    @ApiOperation("新增PointsRuleLog")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsRuleLog", paramType="form", value = "PointsRuleLog的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(PointsRuleLog pointsRuleLog) {
        pointsRuleLogService.insertSelective(pointsRuleLog);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改PointsRuleLog")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsRuleLog", paramType="form", value = "PointsRuleLog的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(PointsRuleLog pointsRuleLog) {
        pointsRuleLogService.updateSelective(pointsRuleLog);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除PointsRuleLog")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "PointsRuleLog的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        pointsRuleLogService.delete(id);
        return BaseOutput.success("删除成功");
    }
}