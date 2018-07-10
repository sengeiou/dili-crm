package com.dili.crm.controller;

import com.dili.crm.domain.IcardUserCard;
import com.dili.crm.service.IcardUserCardService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-30 15:47:56.
 */
@Api("/icardUserCard")
@Controller
@RequestMapping("/icardUserCard")
public class IcardUserCardController {
    @Autowired
    IcardUserCardService icardUserCardService;

    @ApiOperation("跳转到IcardUserCard页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "icardUserCard/index";
    }

    @ApiOperation(value="查询IcardUserCard", notes = "查询IcardUserCard，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="IcardUserCard", paramType="form", value = "IcardUserCard的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<IcardUserCard> list(IcardUserCard icardUserCard) {
        return icardUserCardService.list(icardUserCard);
    }

    @ApiOperation(value="分页查询IcardUserCard", notes = "分页查询IcardUserCard，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="IcardUserCard", paramType="form", value = "IcardUserCard的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(IcardUserCard icardUserCard) throws Exception {
        return icardUserCardService.listEasyuiPageByExample(icardUserCard, true).toString();
    }

    @ApiOperation("新增IcardUserCard")
    @ApiImplicitParams({
		@ApiImplicitParam(name="IcardUserCard", paramType="form", value = "IcardUserCard的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(IcardUserCard icardUserCard) {
        icardUserCardService.insertSelective(icardUserCard);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改IcardUserCard")
    @ApiImplicitParams({
		@ApiImplicitParam(name="IcardUserCard", paramType="form", value = "IcardUserCard的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(IcardUserCard icardUserCard) {
        icardUserCardService.updateSelective(icardUserCard);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除IcardUserCard")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "IcardUserCard的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        icardUserCardService.delete(id);
        return BaseOutput.success("删除成功");
    }
}