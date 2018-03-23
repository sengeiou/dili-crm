package com.dili.crm.api;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.dto.CustomerApiDTO;
import com.dili.crm.service.CustomerService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    BaseOutput<List<Customer>> list(CustomerApiDTO customer) {
        return BaseOutput.success().setData(customerService.listByExample(customer));
    }

    @ApiOperation(value = "分页查询customer列表接口", notes = "分页查询customer列表接口，返回列表信息")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Customer", paramType = "form", value = "Customer的form信息", required = false, dataType = "string") })
    @RequestMapping(value = "/listPage", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    BaseOutput<String> listPage(CustomerApiDTO customer) throws Exception {
        return BaseOutput.success().setData(customerService.listEasyuiPageByExample(customer, false).toString());
    }
}
