package com.dili.points.controller;

import com.dili.points.domain.Order;
import com.dili.points.service.OrderService;
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
 * This file was generated on 2018-03-21 16:01:59.
 */
@Api("/order")
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @ApiOperation("跳转到Order页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "order/index";
    }

    @ApiOperation(value="查询Order", notes = "查询Order，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Order", paramType="form", value = "Order的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Order> list(Order order) {
        return orderService.list(order);
    }

    @ApiOperation(value="分页查询Order", notes = "分页查询Order，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Order", paramType="form", value = "Order的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Order order) throws Exception {
        return orderService.listEasyuiPageByExample(order, true).toString();
    }

    @ApiOperation("新增Order")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Order", paramType="form", value = "Order的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Order order) {
        orderService.insertSelective(order);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改Order")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Order", paramType="form", value = "Order的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Order order) {
        orderService.updateSelective(order);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Order")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Order的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        orderService.delete(id);
        return BaseOutput.success("删除成功");
    }
}