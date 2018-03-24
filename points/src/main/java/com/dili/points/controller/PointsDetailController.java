package com.dili.points.controller;

import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.dto.CustomerPointsDTO;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.points.service.CustomerPointsService;
import com.dili.points.service.PointsDetailService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
@Api("/pointsDetail")
@Controller
@RequestMapping("/pointsDetail")
public class PointsDetailController {
    @Autowired
    PointsDetailService pointsDetailService;
    @Autowired CustomerPointsService customerPointsService;

    @ApiOperation("跳转到PointsDetail页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "pointsDetail/index";
    }
    @ApiOperation("跳转到detail页面")
    @RequestMapping(value="/detail.html", method = RequestMethod.GET)
    public String detail(ModelMap modelMap,String certificateNumber) {
    	CustomerPointsDTO customerPoints=this.customerPointsService.findCustomerPointsByCertificateNumber(certificateNumber);
    	modelMap.put("customerPoints", customerPoints);
        return "pointsDetail/detail";
    }
    @ApiOperation("跳转到manullyDetail页面")
    @RequestMapping(value="/manullyDetail.html", method = RequestMethod.GET)
    public String manullyDetail(ModelMap modelMap) {
        return "pointsDetail/manullyDetail";
    }
    @ApiOperation(value="查询PointsDetail", notes = "查询PointsDetail，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsDetail", paramType="form", value = "PointsDetail的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<PointsDetail> list(PointsDetail pointsDetail) {
        return pointsDetailService.list(pointsDetail);
    }

    @ApiOperation(value="分页查询PointsDetail", notes = "分页查询PointsDetail，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsDetail", paramType="form", value = "PointsDetail的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(PointsDetail pointsDetail) throws Exception {
        return pointsDetailService.listEasyuiPageByExample(pointsDetail, true).toString();
    }

    
    @ApiOperation("手工调整PointsDetail")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsDetail", paramType="form", value = "PointsDetail的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/mannuallyInsert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput mannuallyInsert(PointsDetailDTO pointsDetail) {
    	//先进行基本属性判断
    	//积分不能为空
    	if(pointsDetail.getPoints()==null) {
    		
    	}
    	//证件号码不能为空
    	if(StringUtils.isBlank(pointsDetail.getCertificateNumber())) {
    		
    	}
    	
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if (userTicket == null) {
			throw new RuntimeException("未登录");
		}
		pointsDetail.setCreatedId(userTicket.getId());//操作人
    	pointsDetail.setGenerateWay(50);//50 手工调整
        pointsDetailService.insert(pointsDetail);
        return BaseOutput.success("新增成功");
    }
    
    @ApiOperation("新增PointsDetail")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsDetail", paramType="form", value = "PointsDetail的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(PointsDetail pointsDetail) {
        pointsDetailService.insertSelective(pointsDetail);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改PointsDetail")
    @ApiImplicitParams({
		@ApiImplicitParam(name="PointsDetail", paramType="form", value = "PointsDetail的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(PointsDetail pointsDetail) {
        pointsDetailService.updateSelective(pointsDetail);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除PointsDetail")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "PointsDetail的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        pointsDetailService.delete(id);
        return BaseOutput.success("删除成功");
    }
}