package com.dili.points.api;

import com.alibaba.fastjson.JSONObject;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.PointsDetail;
import com.dili.points.domain.dto.CustomerPointsApiDTO;
import com.dili.points.service.CustomerPointsService;
import com.dili.points.service.PointsDetailService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
        String certificateNumber = jsonObject.getString("certificateNumber");
        if(StringUtils.isBlank(certificateNumber)){
            return BaseOutput.success();
        }
        CustomerPoints customerPoints = DTOUtils.newDTO(CustomerPoints.class);
        customerPoints.setYn(1);
        customerPoints.setCertificateNumber(certificateNumber);
        List<CustomerPoints> customerPointss = customerPointsService.listByExample(customerPoints);
        CustomerPoints result = null;
        if(customerPointss != null && !customerPointss.isEmpty()){
            result = customerPointss.get(0);
        }
        return BaseOutput.success().setData(result);
    }

    @ApiOperation(value = "根据客户证件号码查询客户积分明细", notes = "根据客户证件号码查询客户积分明细")
    @ApiImplicitParams({ @ApiImplicitParam(name = "certificateNumber JSON", paramType = "form", value = "证件号码JSON信息", dataType = "string") })
    @RequestMapping(value = "/listPointsDetail", method = { RequestMethod.POST })
    public @ResponseBody
    BaseOutput<CustomerPoints> listPointsDetail(@RequestBody String paramJson) {
        JSONObject jsonObject = JSONObject.parseObject(paramJson);
        PointsDetail pointsDetail = DTOUtils.newDTO(PointsDetail.class);
        String certificateNumber = jsonObject.getString("certificateNumber");
        if(StringUtils.isBlank(certificateNumber)){
            return BaseOutput.success();
        }
        String page = jsonObject.getString("page");
        if(StringUtils.isBlank(page)){
            pointsDetail.setPage(1);
        }else{
            pointsDetail.setPage(Integer.parseInt(page));
        }
        String rows = jsonObject.getString("rows");
        if(StringUtils.isBlank(rows)){
            pointsDetail.setRows(50);
        }else{
            pointsDetail.setRows(Integer.parseInt(rows));
        }
        pointsDetail.setCertificateNumber(certificateNumber);
        pointsDetail.setOrder("desc");
        pointsDetail.setSort("`created`");
        List<PointsDetail> pointsDetails = pointsDetailService.listByExample(pointsDetail);
        return BaseOutput.success().setData(pointsDetails);
    }

    @ApiOperation(value = "根据客户证件号码查询客户积分信息", notes = "根据客户证件号码查询客户积分信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "CustomerPointsApiDTO", paramType = "form", value = "客户积分条件对象", dataType = "string") })
    @RequestMapping(value = "/listCustomerPoints", method = { RequestMethod.POST })
    public @ResponseBody
    BaseOutput<List<CustomerPoints>> listCustomerPoints(CustomerPointsApiDTO customerPointsApiDTO) {
    	customerPointsApiDTO.setYn(1);
        return BaseOutput.success().setData(customerPointsService.listByExample(customerPointsApiDTO));
    }
    
    
    @ApiOperation(value = "根据客户ID逻辑删除客户积分信息", notes = "根据客户ID逻辑删除客户积分信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "customerId Long", paramType = "form", value = "客户ID", dataType = "Long") })
    @RequestMapping(value = "/deleteCustomerPoints", method = { RequestMethod.POST })
    public @ResponseBody
    BaseOutput<CustomerPoints> deleteCustomerPoints(@RequestBody Long customerId) {
    	CustomerPoints customerPoints=customerPointsService.get(customerId);
    	if(customerPoints!=null&&Integer.valueOf(1).equals(customerPoints.getYn())) {
    		customerPoints.setYn(0);
    		this.customerPointsService.update(customerPoints);
    		 return BaseOutput.success().setData(customerPoints);
    	}
    	 return BaseOutput.failure("更新客户积分信息失败");
        
       
    }
}
