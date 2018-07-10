package com.dili.points.controller;

import com.dili.points.domain.Category;
import com.dili.points.service.CategoryService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2018-03-20 14:29:31.
 */
@Api("/category")
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @ApiOperation("跳转到Category页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "category/index";
    }

    @ApiOperation(value="查询Category", notes = "查询Category，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Category", paramType="form", value = "Category的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<Category> list(Category category) {
        List<Category> list = categoryService.list(category);
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        Category defaultValue = DTOUtils.newDTO(Category.class);
        defaultValue.setCategoryId(null);
        defaultValue.setName("-- 请选择 --");
        list.add(0, defaultValue);
        return list;
    }

    @ApiOperation(value="分页查询Category", notes = "分页查询Category，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Category", paramType="form", value = "Category的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(Category category) throws Exception {
        return categoryService.listEasyuiPageByExample(category, true).toString();
    }

    @ApiOperation("新增Category")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Category", paramType="form", value = "Category的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(Category category) {
        categoryService.insertSelective(category);
        return BaseOutput.success("新增成功");
    }

    @ApiOperation("修改Category")
    @ApiImplicitParams({
		@ApiImplicitParam(name="Category", paramType="form", value = "Category的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(Category category) {
        categoryService.updateSelective(category);
        return BaseOutput.success("修改成功");
    }

    @ApiOperation("删除Category")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "Category的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        categoryService.delete(id);
        return BaseOutput.success("删除成功");
    }
}