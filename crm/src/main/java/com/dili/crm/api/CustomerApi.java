package com.dili.crm.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.crm.constant.CrmConstants;
import com.dili.crm.domain.Customer;
import com.dili.crm.domain.dto.CustomerApiDTO;
import com.dili.crm.domain.dto.CustomerDto;
import com.dili.crm.service.CustomerService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.quartz.domain.ScheduleMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <B>客户信息相关的API</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/3/21 14:11
 */
@Api("/customerApi")
@RestController
@RequestMapping("/customerApi")
public class CustomerApi {

    @Autowired
    private CustomerService customerService;

    @ApiOperation(value = "查询customer列表接口", notes = "查询customer列表接口，返回列表信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Customer", paramType = "form", value = "Customer的form信息", required = false, dataType = "string") })
    @RequestMapping(value = "/list.api", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    BaseOutput<List<Customer>> list(CustomerApiDTO customer) {
        return BaseOutput.success().setData(customerService.listByExample(customer));
    }

    @ApiOperation(value = "分页查询customer列表接口", notes = "分页查询customer列表接口，返回列表信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Customer", paramType = "form", value = "Customer的form信息", required = false, dataType = "string") })
    @RequestMapping(value = "/listPage.api", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    BaseOutput<EasyuiPageOutput> listPage(CustomerApiDTO customer) throws Exception {
        return BaseOutput.success().setData(customerService.listEasyuiPageByExample(customer, true));
    }

    @ApiOperation(value = "按照条件扫描临时用户数据", notes = "按照条件扫描临时用户数据，并级联删除")
    @ApiImplicitParams({ @ApiImplicitParam(name = "scheduleMessage", paramType = "form", value = "scheduleMessage消息", required = false, dataType = "string") })
    @RequestMapping(value = "/scanTempCustomer.api", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public BaseOutput scanTempCustomer(@RequestBody ScheduleMessage scheduleMessage) throws Exception {
        CustomerDto customer = DTOUtils.newDTO(CustomerDto.class);
        customer.setYn(0);
        customer.setEqualName("temp");
        if (null != scheduleMessage && StringUtils.isNotBlank(scheduleMessage.getJobData())) {
            JSONObject object = JSON.parseObject(scheduleMessage.getJobData());
            //数据库中配置的扫描多长时间以前的数据(单位：小时)
            if (object.containsKey("scanTime")) {
                Integer time = object.getInteger("scanTime");
                if (null != time && time.intValue() > 0) {
                    CrmConstants.scanTime = time * 60 * 60;
                }
            }
        }
        customer.setMarket(CrmConstants.ALL_MARKET);
        customer.setCreatedEnd(DateUtils.addSeconds(new Date(), 0 - CrmConstants.scanTime));
        List<Customer> customers = customerService.listByExample(customer);
        if (CollectionUtils.isNotEmpty(customers)){
            List<Long> ids = customers.stream().map(Customer::getId).collect(Collectors.toList());
            customerService.deleteWithCascade(ids);
        }
        return BaseOutput.success();
    }


    @ApiOperation(value = "查询单个customer信息接口", notes = "根据证件号查询单个customer信息接口，返回客户信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "certificateNumber", paramType = "body", value = "Customer的certificateNumber信息", required = false, dataType = "string")})
    @RequestMapping(value = "/getByCertificateNumber.api", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<Customer> getByCertificateNumber(@RequestBody String certificateNumber) {
        if (StringUtils.isNotBlank(certificateNumber)) {
            Customer customer = DTOUtils.newDTO(Customer.class);
            customer.setCertificateNumber(certificateNumber);
            customer.setYn(1);
            List<Customer> customers = customerService.list(customer);
            if (CollectionUtils.isNotEmpty(customers)) {
                if (customers.size() > 1) {
                    return BaseOutput.failure("根据唯一证件号检索出多条结果，请核对数据。");
                } else {
                    return BaseOutput.success().setData(customers.get(0));
                }
            }
        }
        return BaseOutput.success();
    }
}
