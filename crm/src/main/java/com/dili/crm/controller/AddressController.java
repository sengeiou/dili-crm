package com.dili.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.dili.crm.domain.Address;
import com.dili.crm.rpc.MapRpc;
import com.dili.crm.service.AddressService;
import com.dili.crm.service.CityService;
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

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-15 11:16:14.
 */
@Api("/address")
@Controller
@RequestMapping("/address")
public class AddressController {
    @Autowired
    AddressService addressService;
    @Autowired CityService cityService;

    @Resource MapRpc mapRpc;
    @ApiOperation("跳转到Address页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "address/index";
    }

    @ApiOperation(value="查询Address", notes = "查询Address，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Address", paramType="form", value = "Address的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Address> list(Address address) {
        return addressService.list(address);
    }

    @ApiOperation(value="分页查询Address", notes = "分页查询Address，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Address", paramType="form", value = "Address的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Address address) throws Exception {
        Map<String, Object> metadata = new HashMap<>();
        JSONObject cityProvider = new JSONObject();
        cityProvider.put("provider", "cityProvider");
        metadata.put("cityId", cityProvider);
        address.mset(metadata);
        return addressService.listEasyuiPageByExample(address, true).toString();
    }

    @ApiOperation("新增Address")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Address", paramType="form", value = "Address的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Address address) throws Exception{
        return addressService.insertSelectiveWithOutput(address);
    }

    @ApiOperation("修改Address")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Address", paramType="form", value = "Address的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Address address) throws Exception{
        return addressService.updateSelectiveWithOutput(address);
    }

    @ApiOperation("删除Address")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Address的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        addressService.delete(id);
        return BaseOutput.success("删除成功");
    }
    
    @ApiOperation("获取逆地理编码")
    @ApiImplicitParams({
		@ApiImplicitParam(name="lat", paramType="form", value = "纬度", required = true, dataType = "String"),
		@ApiImplicitParam(name="lng", paramType="form", value = "经度", required = true, dataType = "String")
	})
    @RequestMapping(value="/locationReverse", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput locationReverse(String lat,String lng) throws Exception{
        return this.addressService.locationReverse(lat, lng);
    }
}