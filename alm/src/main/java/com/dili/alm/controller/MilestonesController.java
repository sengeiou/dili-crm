package com.dili.alm.controller;

import com.dili.alm.constant.AlmConstants;
import com.dili.alm.domain.Files;
import com.dili.alm.domain.Milestones;
import com.dili.alm.domain.MilestonesDto;
import com.dili.alm.service.MilestonesService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.sysadmin.sdk.session.SessionContext;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-10-20 11:02:17.
 */
@Api("/milestones")
@Controller
@RequestMapping("/milestones")
public class MilestonesController {
    @Autowired
    MilestonesService milestonesService;

    @ApiOperation("跳转到Milestones页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "milestones/index";
    }

	@ApiOperation("跳转到Files页面")
	@RequestMapping(value="/files.html", method = RequestMethod.GET)
	public String files(Files files, ModelMap modelMap, HttpServletRequest request) {
		request.setAttribute("milestonesId", files.getMilestonesId());
		return "milestones/files";
	}

    @ApiOperation(value="查询Milestones", notes = "查询Milestones，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Milestones", paramType="form", value = "Milestones的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Milestones> list(Milestones milestones) {
        return milestonesService.list(milestones);
    }

    @ApiOperation(value="分页查询Milestones", notes = "分页查询Milestones，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Milestones", paramType="form", value = "Milestones的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Milestones milestones) throws Exception {
	    SessionContext sessionContext = SessionContext.getSessionContext();
	    if(sessionContext == null) {
	    	throw new RuntimeException("未登录");
	    }
	    List<Map> dataauth = sessionContext.dataAuth(AlmConstants.DATA_AUTH_TYPE_PROJECT);
	    List<Long> projectIds = new ArrayList<>(dataauth.size());
	    dataauth.forEach( t -> {
	    	projectIds.add(Long.parseLong(t.get("dataId").toString()));
	    });
	    MilestonesDto milestonesDto = DTOUtils.as(milestones, MilestonesDto.class);
	    if(projectIds.isEmpty()){
		    return new EasyuiPageOutput(0, null).toString();
	    }
	    milestonesDto.setProjectIds(projectIds);
        return milestonesService.listEasyuiPageByExample(milestonesDto, true).toString();
    }

    @ApiOperation("新增Milestones")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Milestones", paramType="form", value = "Milestones的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Milestones milestones) {
        milestonesService.insertSelective(milestones);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改Milestones")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Milestones", paramType="form", value = "Milestones的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Milestones milestones) {
        milestonesService.updateSelective(milestones);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Milestones")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Milestones的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        int rowCnt = milestonesService.delete(id);
        if(rowCnt > 0) {
	        return BaseOutput.success("删除成功");
        }else{
	        return BaseOutput.failure("删除失败, 请先删除子里程碑");
        }
    }
}