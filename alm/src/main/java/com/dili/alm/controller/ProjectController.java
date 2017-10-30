package com.dili.alm.controller;

import com.alibaba.fastjson.JSONObject;
import com.dili.alm.constant.AlmConstants;
import com.dili.alm.domain.Project;
import com.dili.alm.domain.dto.DataDictionaryValueDto;
import com.dili.alm.domain.dto.ProjectDto;
import com.dili.alm.rpc.UserRpc;
import com.dili.alm.service.ProjectService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.sysadmin.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-10-18 17:22:54.
 */
@Api("/project")
@Controller
@RequestMapping("/project")
public class ProjectController {
	@Autowired
	ProjectService projectService;
	@Autowired
	private UserRpc userRPC;

	@ApiOperation("跳转到Project页面")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		return "project/index";
	}

	@ApiOperation(value = "查询Project", notes = "查询Project，返回列表信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "Project", paramType = "form", value = "Project的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<Project> list(Project project) {
		Map<Object, Object> metadata = new HashMap<>();

		JSONObject projectTypeProvider = new JSONObject();
		projectTypeProvider.put("provider", "projectTypeProvider");
		metadata.put("type", projectTypeProvider);

		JSONObject datetimeProvider = new JSONObject();
		datetimeProvider.put("provider", "datetimeProvider");
		metadata.put("created", datetimeProvider);
		metadata.put("modified", datetimeProvider);

		JSONObject memberProvider = new JSONObject();
		memberProvider.put("provider", "memberProvider");
		metadata.put("projectManager", memberProvider);
		metadata.put("testManager", memberProvider);
		metadata.put("productManager", memberProvider);

		// 测试数据
		List<Project> list = this.projectService.listByExample(project);
		try {
			return ValueProviderUtils.buildDataByProvider(metadata, list);
		} catch (Exception e) {
			return null;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/list.json", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Project> listJson(Project project) {
		return this.projectService.list(project);
	}

	@ApiOperation(value = "分页查询Project", notes = "分页查询Project，返回easyui分页信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "Project", paramType = "form", value = "Project的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(Project project) throws Exception {
		SessionContext sessionContext = SessionContext.getSessionContext();
		if(sessionContext == null) {
			throw new RuntimeException("未登录");
		}
		List<Map> dataauth = sessionContext.dataAuth(AlmConstants.DATA_AUTH_TYPE_PROJECT);
		List<Long> projectIds = new ArrayList<>(dataauth.size());
		dataauth.forEach( t -> {
			projectIds.add(Long.parseLong(t.get("dataId").toString()));
		});
		ProjectDto projectDto = DTOUtils.as(project, ProjectDto.class);
		if(projectIds.isEmpty()){
			return new EasyuiPageOutput(0, null).toString();
		}
		projectDto.setIds(projectIds);
		return projectService.listEasyuiPageByExample(projectDto, true).toString();
	}

	@ApiOperation("新增Project")
	@ApiImplicitParams({ @ApiImplicitParam(name = "Project", paramType = "form", value = "Project的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/insert", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput insert(Project project) {
		projectService.insertSelective(project);
		return BaseOutput.success("新增成功").setData(project);
	}

	@ApiOperation("修改Project")
	@ApiImplicitParams({ @ApiImplicitParam(name = "Project", paramType = "form", value = "Project的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/update", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(Project project) {
		projectService.updateSelective(project);
		return BaseOutput.success("修改成功").setData(project);
	}

	@ApiOperation("删除Project")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", paramType = "form", value = "Project的主键", required = true, dataType = "long") })
	@RequestMapping(value = "/delete", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput<Object> delete(Long id) {
		return projectService.deleteBeforeCheck(id);
	}

	@ResponseBody
	@RequestMapping(value = "/type.json", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<DataDictionaryValueDto> projectTypeJson() {
		return this.projectService.getPojectTypes();
	}

}