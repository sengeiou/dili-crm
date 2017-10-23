package com.dili.alm.controller;

import com.dili.alm.domain.ScheduleJob;
import com.dili.alm.service.ScheduleJobService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.ConditionItems;
import com.dili.ss.servlet.RefreshCSRFToken;
import com.dili.ss.servlet.VerifyCSRFToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-06-06 10:27:25.
 */
@Api(value = "scheduleJob-api", description = "调度器操作操作", position = 1)
@Controller
@RequestMapping("/scheduleJob")
public class ScheduleJobController {
    @Autowired
    ScheduleJobService scheduleJobService;


    @RefreshCSRFToken
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "scheduleJob/index";
    }

    @RefreshCSRFToken
    @RequestMapping(value = "/index2", method = RequestMethod.GET)
    public String index2(ModelMap modelMap) {
        return "scheduleJob/index2";
    }

    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<ScheduleJob> list(@ModelAttribute ScheduleJob scheduleJob) {
        return scheduleJobService.list(scheduleJob);
    }

    @ApiOperation(value="查询调度工作", notes="分页查询，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scheduleJob", paramType="form", value = "com.dili.alm.domain.ScheduleJob的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute ScheduleJob scheduleJob) throws Exception {
        return scheduleJobService.listEasyuiPageByExample(scheduleJob, true).toString();
    }

    @RequestMapping(value="/listPageByConditionItems", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPageByConditionItems(@ModelAttribute ConditionItems conditionItems) throws Exception {
        if(conditionItems.getConditionRelationField() == null || conditionItems.getConditionItems() == null) {
            return null;
        }
        for(Object obj : conditionItems.getConditionItems()){
            String[] condition = String.valueOf(obj).split(":");
            String conditionField = condition[0];
            String relationField = condition[1];
            String conditionValueField = condition[2];


        }

        ScheduleJob scheduleJob = new ScheduleJob();
        return scheduleJobService.listEasyuiPageByExample(scheduleJob, true).toString();
    }

    @VerifyCSRFToken
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute ScheduleJob scheduleJob) {
        scheduleJobService.insertSelective(scheduleJob);
        return BaseOutput.success("新增成功");
    }

    @VerifyCSRFToken
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute ScheduleJob scheduleJob) {
        scheduleJobService.updateSelective(scheduleJob);
        return BaseOutput.success("修改成功");
    }
    @VerifyCSRFToken
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        scheduleJobService.delete(id);
        return BaseOutput.success("删除成功");
    }


}