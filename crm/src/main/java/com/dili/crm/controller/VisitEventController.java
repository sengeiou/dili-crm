package com.dili.crm.controller;

import com.dili.crm.domain.VisitEvent;
import com.dili.crm.service.VisitEventService;
import com.dili.ss.domain.BaseOutput;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
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
 * This file was generated on 2017-11-21 16:39:42.
 */
@Api("/visitEvent")
@Controller
@RequestMapping("/visitEvent")
public class VisitEventController {
    @Autowired
    VisitEventService visitEventService;

    @ApiOperation("跳转到VisitEvent页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "visitEvent/index";
    }

    @ApiOperation(value="查询VisitEvent", notes = "查询VisitEvent，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="VisitEvent", paramType="form", value = "VisitEvent的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<VisitEvent> list(VisitEvent visitEvent) {
        return visitEventService.list(visitEvent);
    }

    @ApiOperation(value="分页查询VisitEvent", notes = "分页查询VisitEvent，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="VisitEvent", paramType="form", value = "VisitEvent的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(VisitEvent visitEvent) throws Exception {
        return visitEventService.listEasyuiPageByExample(visitEvent, true).toString();
    }

    @ApiOperation("新增VisitEvent")
    @ApiImplicitParams({
		@ApiImplicitParam(name="VisitEvent", paramType="form", value = "VisitEvent的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(VisitEvent visitEvent) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            return BaseOutput.failure("新增失败，登录超时");
        }
        visitEvent.setUserId(userTicket.getId());
        visitEventService.insertSelective(visitEvent);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改VisitEvent")
    @ApiImplicitParams({
		@ApiImplicitParam(name="VisitEvent", paramType="form", value = "VisitEvent的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(VisitEvent visitEvent) {
        visitEventService.updateSelective(visitEvent);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除VisitEvent")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "VisitEvent的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        visitEventService.delete(id);
        return BaseOutput.success("删除成功");
    }
}