package com.dili.crm.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.crm.domain.City;
import com.dili.crm.domain.dto.CityDto;
import com.dili.crm.service.CityService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.google.common.collect.Lists;
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

import java.util.HashMap;
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
    	DTO queryDto = DTOUtils.go(city);
	    CityDto cityDto= DTOUtils.as(city, CityDto.class);
    	//如果只有sort和order字段,即没有查询条件的情况下，就只查parentId=0的(中国)
	    if(queryDto.size()<=2){
//		    cityDto.setParentId(0L);
		    //全查时查两级
		    cityDto.setLevelTypes(Lists.newArrayList(0, 1));
	    }
	    //最多查100条，避免页面卡死
//	    city.setRows(100);
//    	city.setPage(1);
	    EasyuiPageOutput easyuiPageOutput = cityService.listEasyuiPageByExample(cityDto, true);
		for(Object rowObj : easyuiPageOutput.getRows()) {
			if(DTOUtils.isDTOProxy(rowObj)){
				DTOUtils.as(rowObj, CityDto.class).setState("closed");
			}else {
				Map rowMap = (Map) rowObj;
				rowMap.put("state", "closed");
			}
		}
		return easyuiPageOutput.toString();
    }

	/**
	 * 展开城市树
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/expand", method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody String expand(Long parentId) throws Exception {
		City city = DTOUtils.newDTO(City.class);
		city.setParentId(parentId);
		Map<String, Object> metadata = new HashMap<>();
		JSONObject cityYnProvider = new JSONObject();
		cityYnProvider.put("provider", "cityYnProvider");
		metadata.put("created", getDatetimeProvider());
		metadata.put("modified", getDatetimeProvider());
		metadata.put("yn", cityYnProvider);
		city.mset(metadata);
		EasyuiPageOutput easyuiPageOutput = cityService.listEasyuiPageByExample(city, true);
		for(Object rowObj : easyuiPageOutput.getRows()) {
			if(DTOUtils.isDTOProxy(rowObj)){
				DTOUtils.as(rowObj, CityDto.class).setState("closed");
			}else {
				Map rowMap = (Map) rowObj;
				rowMap.put("state", "closed");
			}
		}
		return JSONArray.toJSONString(easyuiPageOutput.getRows());
	}

	//获取时间提供者
	private JSONObject getDatetimeProvider(){
		JSONObject datetimeProvider = new JSONObject();
		datetimeProvider.put("provider", "datetimeProvider");
		return datetimeProvider;
	}

    @ApiOperation("新增City")
    @ApiImplicitParams({
		@ApiImplicitParam(name="City", paramType="form", value = "City的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(City city) {
	    city.setYn(true);
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