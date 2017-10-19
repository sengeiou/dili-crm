package com.dili.alm.controller;

import com.dili.alm.domain.Project;
import com.dili.alm.service.ProjectService;
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
 * This file was generated on 2017-10-18 17:22:54.
 */
@Api("/project")
@Controller
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @ApiOperation("跳转到Project页面")
    @RequestMapping(value="/index", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "project/index";
    }

    @ApiOperation(value="查询Project", notes = "查询Project，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Project", paramType="form", value = "Project的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Project> list(Project project) {
        return projectService.list(project);
    }

    @ApiOperation(value="分页查询Project", notes = "分页查询Project，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Project", paramType="form", value = "Project的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Project project) throws Exception {
        return projectService.listEasyuiPageByExample(project, true).toString();
    }

    @ApiOperation("新增Project")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Project", paramType="form", value = "Project的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Project project) {
        projectService.insertSelective(project);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改Project")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Project", paramType="form", value = "Project的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Project project) {
        projectService.updateSelective(project);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Project")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Project的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        projectService.delete(id);
        return BaseOutput.success("删除成功");
    }
}