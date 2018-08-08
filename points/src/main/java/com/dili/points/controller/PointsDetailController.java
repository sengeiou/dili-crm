package com.dili.points.controller;

import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.points.service.CustomerFirmPointsService;
import com.dili.points.service.FirmService;
import com.dili.points.service.PointsDetailService;
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
import java.util.Map;

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
    @Autowired
    CustomerFirmPointsService customerFirmPointsService;
    @Autowired
    FirmService firmService;

    @ApiOperation("跳转到PointsDetail页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "pointsDetail/index";
    }

    @ApiOperation("跳转到积分明细页面")
    @RequestMapping(value = "/adjustRecord.html", method = RequestMethod.GET)
    public String adjustRecord(ModelMap modelMap) {
        return "pointsDetail/adjustRecord";
    }

    @ApiOperation("跳转到detail页面")
    @RequestMapping(value = "/detail.html", method = RequestMethod.GET)
    public String detail(ModelMap modelMap, String certificateNumber) {
        Map customerFirmPoints = this.customerFirmPointsService.findCustomerFirmPointsByCertificateNumber(certificateNumber);
        modelMap.put("customerPoints", customerFirmPoints);
        return "pointsDetail/detail";
    }

    @ApiOperation("跳转到manullyDetail页面")
    @RequestMapping(value = "/manullyDetail.html", method = RequestMethod.GET)
    public String manullyDetail(ModelMap modelMap) {
        return "pointsDetail/manullyDetail";
    }

    @ApiOperation(value = "查询PointsDetail", notes = "查询PointsDetail，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsDetail", paramType = "form", value = "PointsDetail的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<PointsDetail> list(PointsDetail pointsDetail) {
        return pointsDetailService.list(pointsDetail);
    }

    @ApiOperation(value = "分页查询PointsDetail", notes = "分页查询PointsDetail，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsDetail", paramType = "form", value = "PointsDetail的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(PointsDetailDTO pointsDetail) throws Exception {
        return this.pointsDetailService.listEasyuiPageByExample(pointsDetail, true, this.firmService.getCurrentUserFirmCodes()).toString();
    }

    @ApiOperation(value = "分页查询调整记录", notes = "分页查询调整记录，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsDetail", paramType = "form", value = "PointsDetailDTO的form信息", required = false, dataType = "string")
    })
    @RequestMapping(value = "/listAdjustRecordPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listAdjustRecordPage(PointsDetailDTO pointsDetail) throws Exception {
        pointsDetail.setGenerateWay(50);
        List<String> firmCodes = this.firmService.getCurrentUserAvaliableFirmCodes(pointsDetail.getFirmCode());
        return this.pointsDetailService.listEasyuiPageByExample(pointsDetail, true, firmCodes).toString();
    }

    @ApiOperation("新增PointsDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsDetail", paramType = "form", value = "PointsDetail的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput insert(PointsDetail pointsDetail) {
        pointsDetailService.insertSelective(pointsDetail);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改PointsDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PointsDetail", paramType = "form", value = "PointsDetail的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(PointsDetail pointsDetail) {
        pointsDetailService.updateSelective(pointsDetail);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除PointsDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "PointsDetail的主键", required = true, dataType = "long")
    })
    @RequestMapping(value = "/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput delete(Long id) {
        pointsDetailService.delete(id);
        return BaseOutput.success("删除成功");
    }
}