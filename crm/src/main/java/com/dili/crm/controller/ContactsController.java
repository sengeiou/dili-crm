package com.dili.crm.controller;

import com.dili.crm.domain.Contacts;
import com.dili.crm.service.ContactsService;
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
 * This file was generated on 2017-11-28 14:49:44.
 */
@Api("/contacts")
@Controller
@RequestMapping("/contacts")
public class ContactsController {
    @Autowired
    ContactsService contactsService;

    @ApiOperation("跳转到Contacts页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "contacts/index";
    }

    @ApiOperation(value="查询Contacts", notes = "查询Contacts，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Contacts", paramType="form", value = "Contacts的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Contacts> list(Contacts contacts) {
        return contactsService.list(contacts);
    }

    @ApiOperation(value="分页查询Contacts", notes = "分页查询Contacts，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Contacts", paramType="form", value = "Contacts的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Contacts contacts) throws Exception {
        return contactsService.listEasyuiPageByExample(contacts, true).toString();
    }

    @ApiOperation("新增Contacts")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Contacts", paramType="form", value = "Contacts的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Contacts contacts) {
        contactsService.insertSelective(contacts);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改Contacts")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Contacts", paramType="form", value = "Contacts的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Contacts contacts) {
        contactsService.updateSelective(contacts);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Contacts")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Contacts的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        contactsService.delete(id);
        return BaseOutput.success("删除成功");
    }
}