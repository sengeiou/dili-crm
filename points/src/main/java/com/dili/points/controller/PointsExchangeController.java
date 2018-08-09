package com.dili.points.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.points.constant.PointsConstants;
import com.dili.points.domain.Customer;
import com.dili.points.domain.CustomerFirmPoints;
import com.dili.points.domain.CustomerPoints;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.domain.dto.FirmDto;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.service.CustomerFirmPointsService;
import com.dili.points.service.FirmService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <B>积分兑换功能</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/3/22 16:14
 */
@Api("/pointsExchange")
@Controller
@RequestMapping("/pointsExchange")
public class PointsExchangeController {

    @Autowired
    private CustomerRpc customerRpc;
    @Autowired
    private CustomerFirmPointsService customerFirmPointsService;

    @Autowired
    private FirmService firmService;

    @ApiOperation("跳转到PointsExchange页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "pointsExchange/index";
    }

    @ApiOperation(value="根据证件号远程查询客户信息", notes = "根据证件号远程查询客户信息")
    @RequestMapping(value="/getCustomerInfo.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput getCustomerInfo(@RequestParam String certificateNumber, @RequestParam String firmCode) throws Exception {
        if (StringUtils.isBlank(certificateNumber)){
            return null;
        }
        CustomerApiDTO dto = DTOUtils.newDTO(CustomerApiDTO.class);
        dto.setCertificateNumber(certificateNumber);
        dto.setYn(1);
        //查询所有市场的客户
        dto.setMarket(PointsConstants.ALL_MARKET);
        BaseOutput<List<Customer>> baseOutput = customerRpc.list(dto);
        if (baseOutput.isSuccess() && CollectionUtils.isNotEmpty(baseOutput.getData())){
            JSONObject object = JSONObject.parseObject(JSON.toJSONString(baseOutput.getData().get(0)));
            //获取客户积分
            CustomerFirmPoints cp = DTOUtils.newDTO(CustomerFirmPoints.class);
            cp.setCertificateNumber(certificateNumber);
            cp.setTradingFirmCode(firmCode);
            List<CustomerFirmPoints> points = customerFirmPointsService.listByExample(cp);
            if (CollectionUtils.isNotEmpty(points)){
                object.put("points",points.get(0).getAvailable());
            }else{
                object.put("points",0);
            }
            object.put("tradingFirmCode", firmCode);
            return BaseOutput.success().setData(object);
        }
        return BaseOutput.failure("未获取到用户信息");
    }
}
