package com.dili.crm.controller;

import com.alibaba.fastjson.JSONArray;
import com.dili.crm.domain.City;
import com.dili.crm.service.CityService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
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
 * This file was generated on 2017-11-14 15:11:53.
 */
@Api("/city")
@Controller
@RequestMapping("/city")
public class CityController {
    @Autowired
    CityService cityService;

    @ApiOperation("跳转到City页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "city/index";
    }

    @ApiOperation(value="查询City", notes = "查询City，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="City", paramType="form", value = "City的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<City> list(City city) {
        return cityService.list(city);
    }

    @ApiOperation(value="分页查询City", notes = "分页查询City，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="City", paramType="form", value = "City的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(City city) throws Exception {
    	List<City> list = cityService.listByExample(city);
		List<Map> results = ValueProviderUtils.buildDataByProvider(city, list);
		for(Map c : results) {
			c.put("state", "closed");
		}
		return JSONArray.toJSONString(results);
    }

    @ApiOperation("新增City")
    @ApiImplicitParams({
		@ApiImplicitParam(name="City", paramType="form", value = "City的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(City city) {
        cityService.insertSelective(city);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改City")
    @ApiImplicitParams({
		@ApiImplicitParam(name="City", paramType="form", value = "City的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(City city) {
        cityService.updateSelective(city);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除City")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "City的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        cityService.delete(id);
        return BaseOutput.success("删除成功");
    }
}