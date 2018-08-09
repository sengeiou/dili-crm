package com.dili.points.controller;

import com.dili.points.constant.PointsConstants;
import com.dili.points.domain.CustomerFirmPoints;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.domain.dto.PointsDetailDTO;
import com.dili.points.service.CustomerFirmPointsService;
import com.dili.points.service.PointsDetailService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.exception.NotLoginException;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-07-30 16:20:03.
 */
@Api("/customerFirmPoints")
@Controller
@RequestMapping("/customerFirmPoints")
public class CustomerFirmPointsController {
    @Autowired
    CustomerFirmPointsService customerFirmPointsService;
    @Autowired
    PointsDetailService pointsDetailService;

    @ApiOperation("跳转到CustomerFirmPoints页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerFirmPoints/index";
    }

    @ApiOperation(value="分页查询CustomerFirmPoints", notes = "分页查询CustomerFirmPoints，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerFirmPoints", paramType="form", value = "CustomerFirmPoints的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(CustomerApiDTO customer) throws Exception {
        customer.setUserId(SessionContext.getSessionContext().getUserTicket().getId());
        return this.customerFirmPointsService.listCustomerFirmPointsByCustomer(customer).toString();
    }

    @ApiOperation("新增CustomerFirmPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerFirmPoints", paramType="form", value = "CustomerFirmPoints的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(CustomerFirmPoints customerFirmPoints) {
        customerFirmPointsService.insertSelective(customerFirmPoints);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改CustomerFirmPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerFirmPoints", paramType="form", value = "CustomerFirmPoints的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(CustomerFirmPoints customerFirmPoints) {
        customerFirmPointsService.updateSelective(customerFirmPoints);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除CustomerFirmPoints")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "CustomerFirmPoints的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        customerFirmPointsService.delete(id);
        return BaseOutput.success("删除成功");
    }

    @ApiOperation("根据客户及市场，获取客户在此市场的积分信息")
    @RequestMapping(value="/getByCustomerAndFirm.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getByCustomerAndFirm(Long customerId,String firmCode) {
        return BaseOutput.success().setData(customerFirmPointsService.getByCustomerAndFirm(customerId,firmCode));
    }

    @ApiOperation("手工调整PointsDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name="PointsDetail", paramType="form", value = "PointsDetail的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value="/mannuallyAdjust.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput mannuallyAdjust(PointsDetailDTO pointsDetail) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new NotLoginException("未登录");
        }
        //先进行基本属性判断
        if(pointsDetail.getPoints()==null||pointsDetail.getPoints()==0) {
            return BaseOutput.failure("调整积分不能为0");
        }
        if(StringUtils.trimToNull(pointsDetail.getNotes())==null) {
            return BaseOutput.failure("备注不能为空");
        }
        pointsDetail.setCreatedId(userTicket.getId());//操作人
        pointsDetail.setGenerateWay(50);//50 手工调整

        if(pointsDetail.getPoints() > 0) {
            pointsDetail.setInOut(10);// 收入
        } else {
            pointsDetail.setInOut(20);// 支出
        }
        pointsDetail.setSourceSystem(PointsConstants.SYSTEM_CODE);
        pointsDetailService.insert(pointsDetail);
        return BaseOutput.success("新增成功");
    }

    /**
     * 客户积分清零
     * @param notes
     * @return
     */
    @RequestMapping(value="/clearPoints.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput clearPoints(String firmCode,String notes) {
        if (StringUtils.isBlank(firmCode) || StringUtils.trimToNull(notes) == null) {
            return BaseOutput.failure("参数不正确");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            throw new RuntimeException("未登录");
        }
        pointsDetailService.clear(firmCode, notes);
        return BaseOutput.success("新增成功");
    }
}