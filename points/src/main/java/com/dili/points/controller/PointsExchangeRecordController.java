package com.dili.points.controller;

import com.dili.points.domain.PointsExchangeRecord;
import com.dili.points.service.PointsExchangeRecordService;
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
@Api("/pointsExchangeRecord")
@Controller
@RequestMapping("/pointsExchangeRecord")
public class PointsExchangeRecordController {
    @Autowired
    PointsExchangeRecordService pointsExchangeRecordService;

    @ApiOperation("跳转到PointsExchangeRecord页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "pointsExchangeRecord/index";
    }

    @ApiOperation(value="查询PointsExchangeRecord", notes = "查询PointsExchangeRecord，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsExchangeRecord", paramType="form", value = "PointsExchangeRecord的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<PointsExchangeRecord> list(PointsExchangeRecord pointsExchangeRecord) {
        return pointsExchangeRecordService.list(pointsExchangeRecord);
    }

    @ApiOperation(value="分页查询PointsExchangeRecord", notes = "分页查询PointsExchangeRecord，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsExchangeRecord", paramType="form", value = "PointsExchangeRecord的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(PointsExchangeRecord pointsExchangeRecord) throws Exception {
        return pointsExchangeRecordService.listEasyuiPageByExample(pointsExchangeRecord, true).toString();
    }

    @ApiOperation("新增PointsExchangeRecord")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsExchangeRecord", paramType="form", value = "PointsExchangeRecord的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(PointsExchangeRecord pointsExchangeRecord) {
        pointsExchangeRecordService.insertSelective(pointsExchangeRecord);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改PointsExchangeRecord")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsExchangeRecord", paramType="form", value = "PointsExchangeRecord的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(PointsExchangeRecord pointsExchangeRecord) {
        pointsExchangeRecordService.updateSelective(pointsExchangeRecord);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除PointsExchangeRecord")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "PointsExchangeRecord的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        pointsExchangeRecordService.delete(id);
        return BaseOutput.success("删除成功");
    }
}