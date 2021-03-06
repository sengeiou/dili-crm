package com.dili.points.controller;

import com.dili.points.domain.ExchangeCommodities;
import com.dili.points.domain.dto.ExchangeCommoditiesDTO;
import com.dili.points.service.ExchangeCommoditiesService;
import com.dili.points.service.FirmService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 11:29:31.
 */
@Api("/exchangeCommodities")
@Controller
@RequestMapping("/exchangeCommodities")
public class ExchangeCommoditiesController {
    @Autowired
    ExchangeCommoditiesService exchangeCommoditiesService;
    @Autowired
    FirmService firmService;

    @ApiOperation("跳转到ExchangeCommodities页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "exchangeCommodities/index";
    }

    @ApiOperation(value="查询ExchangeCommodities", notes = "查询ExchangeCommodities，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="ExchangeCommodities", paramType="form", value = "ExchangeCommodities的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<ExchangeCommodities> list(ExchangeCommoditiesDTO exchangeCommodities) {
        if (StringUtils.isBlank(exchangeCommodities.getFirmCode()) && CollectionUtils.isEmpty(exchangeCommodities.getFirmCodes())) {
            List<String> firmCodes = this.firmService.getCurrentUserFirmCodes();
            if (firmCodes.isEmpty()) {
                return Collections.emptyList();
            }
            exchangeCommodities.setFirmCodes(firmCodes);
        }
        return exchangeCommoditiesService.list(exchangeCommodities);
    }

    @ApiOperation(value="分页查询ExchangeCommodities", notes = "分页查询ExchangeCommodities，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="ExchangeCommodities", paramType="form", value = "ExchangeCommodities的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(ExchangeCommoditiesDTO exchangeCommodities) throws Exception {
        if (StringUtils.isBlank(exchangeCommodities.getFirmCode()) && CollectionUtils.isEmpty(exchangeCommodities.getFirmCodes())) {
            List<String> firmCodes = this.firmService.getCurrentUserFirmCodes();
            if (firmCodes.isEmpty()) {
                return new EasyuiPageOutput(0, null).toString();
            }
            exchangeCommodities.setFirmCodes(firmCodes);
        }
        return exchangeCommoditiesService.listEasyuiPageByExample(exchangeCommodities, true).toString();
    }

    @ApiOperation("新增ExchangeCommodities")
    @ApiImplicitParams({
		@ApiImplicitParam(name="ExchangeCommodities", paramType="form", value = "ExchangeCommodities的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(ExchangeCommodities exchangeCommodities) {
        return exchangeCommoditiesService.insertSelectiveWithOutput(exchangeCommodities);
    }

    @ApiOperation("修改ExchangeCommodities")
    @ApiImplicitParams({
		@ApiImplicitParam(name="ExchangeCommodities", paramType="form", value = "ExchangeCommodities的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(ExchangeCommodities exchangeCommodities) {
        return exchangeCommoditiesService.updateSelectiveWithOutput(exchangeCommodities);
    }

    @ApiOperation("删除ExchangeCommodities")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "ExchangeCommodities的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        exchangeCommoditiesService.delete(id);
        return BaseOutput.success("删除成功");
    }
}