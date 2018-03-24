package com.dili.points.api;

import com.alibaba.fastjson.JSONObject;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.PointsDetail;
import com.dili.points.service.CustomerPointsService;
import com.dili.points.service.PointsDetailService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <B>客户积分信息相关的API</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/3/21 14:11
 */
@Api("/customerPointsApi")
@RestController
@RequestMapping("/customerPointsApi")
public class CustomerPointsApi {

    @Autowired
    private CustomerPointsService customerPointsService;
    @Autowired
    private PointsDetailService pointsDetailService;

    @ApiOperation(value = "根据客户证件号码查询客户积分信息", notes = "根据客户证件号码查询客户积分信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "certificateNumber JSON", paramType = "form", value = "证件号码JSON信息", dataType = "string") })
    @RequestMapping(value = "/getCustomerPoints", method = { RequestMethod.POST })
    public @ResponseBody
    BaseOutput<CustomerPoints> getCustomerPoints(@RequestBody String paramJson) {
        JSONObject jsonObject = JSONObject.parseObject(paramJson);
        CustomerPoints customerPoints = DTOUtils.newDTO(CustomerPoints.class);
        customerPoints.setCertificateNumber(jsonObject.getString("certificateNumber"));
        List<CustomerPoints> customerPointss = customerPointsService.listByExample(customerPoints);
        if(customerPointss == null){
            customerPointss = Lists.newArrayList();
        }
        return BaseOutput.success().setData(customerPointss.get(0));
    }

    @ApiOperation(value = "根据客户证件号码查询客户积分明细", notes = "根据客户证件号码查询客户积分明细")
    @ApiImplicitParams({ @ApiImplicitParam(name = "certificateNumber JSON", paramType = "form", value = "证件号码JSON信息", dataType = "string") })
    @RequestMapping(value = "/listPointsDetail", method = { RequestMethod.POST })
    public @ResponseBody
    BaseOutput<CustomerPoints> listPointsDetail(@RequestBody String paramJson) {
        JSONObject jsonObject = JSONObject.parseObject(paramJson);
        PointsDetail pointsDetail = DTOUtils.newDTO(PointsDetail.class);
        pointsDetail.setCertificateNumber(jsonObject.getString("certificateNumber"));
        List<PointsDetail> pointsDetails = pointsDetailService.listByExample(pointsDetail);
        return BaseOutput.success().setData(pointsDetails);
    }
}