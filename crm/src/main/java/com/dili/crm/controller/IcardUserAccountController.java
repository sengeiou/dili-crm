package com.dili.crm.controller;

import com.dili.crm.domain.IcardUserAccount;
import com.dili.crm.service.IcardUserAccountService;
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
 * This file was generated on 2017-11-30 15:46:52.
 */
@Api("/icardUserAccount")
@Controller
@RequestMapping("/icardUserAccount")
public class IcardUserAccountController {
    @Autowired
    IcardUserAccountService icardUserAccountService;

    @ApiOperation("跳转到IcardUserAccount页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "icardUserAccount/index";
    }

    @ApiOperation(value="查询IcardUserAccount", notes = "查询IcardUserAccount，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="IcardUserAccount", paramType="form", value = "IcardUserAccount的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<IcardUserAccount> list(IcardUserAccount icardUserAccount) {
        return icardUserAccountService.list(icardUserAccount);
    }

    @ApiOperation(value="分页查询IcardUserAccount", notes = "分页查询IcardUserAccount，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="IcardUserAccount", paramType="form", value = "IcardUserAccount的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(IcardUserAccount icardUserAccount) throws Exception {
        return icardUserAccountService.listEasyuiPageByExample(icardUserAccount, true).toString();
    }

    @ApiOperation("新增IcardUserAccount")
    @ApiImplicitParams({
		@ApiImplicitParam(name="IcardUserAccount", paramType="form", value = "IcardUserAccount的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(IcardUserAccount icardUserAccount) {
        icardUserAccountService.insertSelective(icardUserAccount);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改IcardUserAccount")
    @ApiImplicitParams({
		@ApiImplicitParam(name="IcardUserAccount", paramType="form", value = "IcardUserAccount的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(IcardUserAccount icardUserAccount) {
        icardUserAccountService.updateSelective(icardUserAccount);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除IcardUserAccount")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "IcardUserAccount的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        icardUserAccountService.delete(id);
        return BaseOutput.success("删除成功");
    }
}