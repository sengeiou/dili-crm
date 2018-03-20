package com.dili.points.controller;

import com.dili.points.domain.OrderItem;
import com.dili.points.service.OrderItemService;
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
 * This file was generated on 2018-03-20 11:29:31.
 */
@Api("/orderItem")
@Controller
@RequestMapping("/orderItem")
public class OrderItemController {
    @Autowired
    OrderItemService orderItemService;

    @ApiOperation("跳转到OrderItem页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "orderItem/index";
    }

    @ApiOperation(value="查询OrderItem", notes = "查询OrderItem，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="OrderItem", paramType="form", value = "OrderItem的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<OrderItem> list(OrderItem orderItem) {
        return orderItemService.list(orderItem);
    }

    @ApiOperation(value="分页查询OrderItem", notes = "分页查询OrderItem，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="OrderItem", paramType="form", value = "OrderItem的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(OrderItem orderItem) throws Exception {
        return orderItemService.listEasyuiPageByExample(orderItem, true).toString();
    }

    @ApiOperation("新增OrderItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="OrderItem", paramType="form", value = "OrderItem的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(OrderItem orderItem) {
        orderItemService.insertSelective(orderItem);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改OrderItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="OrderItem", paramType="form", value = "OrderItem的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(OrderItem orderItem) {
        orderItemService.updateSelective(orderItem);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除OrderItem")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "OrderItem的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        orderItemService.delete(id);
        return BaseOutput.success("删除成功");
    }
}