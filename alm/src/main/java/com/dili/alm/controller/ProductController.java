package com.dili.alm.controller;

import com.dili.alm.domain.Product;
import com.dili.alm.service.ProductService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-06-28 15:51:42.
 */
@Api("/product")
@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @ApiOperation("跳转到Product页面")
    @RequestMapping(value="/index", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "product/index";
    }

    @ApiOperation(value="查询Product", notes = "查询Product，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Product", paramType="form", value = "Product的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Product> list(@ModelAttribute Product product) {
        return productService.list(product);
    }

    @ApiOperation(value="分页查询Product", notes = "分页查询Product，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Product", paramType="form", value = "Product的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute Product product) throws Exception {
        return productService.listEasyuiPageByExample(product, true).toString();
    }

    @ApiOperation("新增Product")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Product", paramType="form", value = "Product的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute Product product) {
        productService.insertSelective(product);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改Product")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Product", paramType="form", value = "Product的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute Product product) {
        productService.updateSelective(product);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Product")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Product的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        productService.delete(id);
        return BaseOutput.success("删除成功");
    }
}