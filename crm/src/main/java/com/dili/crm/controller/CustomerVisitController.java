package com.dili.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.dili.crm.domain.CustomerVisit;
import com.dili.crm.service.CustomerVisitService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-21 17:27:41.
 */
@Api("/customerVisit")
@Controller
@RequestMapping("/customerVisit")
public class CustomerVisitController {
    private Logger logger = LoggerFactory.getLogger(CustomerVisitController.class);
    @Autowired
    CustomerVisitService customerVisitService;

    @ApiOperation("跳转到CustomerVisit页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customerVisit/index";
    }

    @ApiOperation(value="查询CustomerVisit", notes = "查询CustomerVisit，返回列表信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerVisit", paramType="form", value = "CustomerVisit的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody List<CustomerVisit> list(CustomerVisit customerVisit) {
        return customerVisitService.list(customerVisit);
    }

    @ApiOperation(value="分页查询CustomerVisit", notes = "分页查询CustomerVisit，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerVisit", paramType="form", value = "CustomerVisit的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(CustomerVisit customerVisit) throws Exception {
        return customerVisitService.listEasyuiPageByExample(customerVisit, true).toString();
    }

    @ApiOperation("新增CustomerVisit")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerVisit", paramType="form", value = "CustomerVisit的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(CustomerVisit customerVisit) {
        try {
            customerVisit.setState(1);
            return customerVisitService.insertSelectiveWithOutput(customerVisit);
        }catch (Exception e){
            logger.error(String.format("回访信息[%s] 新增失败[%s]",customerVisit,e.getMessage()),e);
            return BaseOutput.failure("新增失败，系统异常");
        }
    }

    @ApiOperation("新增并完成CustomerVisit")
    @ApiImplicitParams({
            @ApiImplicitParam(name="CustomerVisit", paramType="form", value = "CustomerVisit的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value="/insertAndFinished", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insertAndFinished(CustomerVisit customerVisit) {
        try {
            customerVisit.setState(3);
            return customerVisitService.insertSelectiveWithOutput(customerVisit);
        }catch (Exception e){
            logger.error(String.format("回访信息[%s] 新增失败[%s]",customerVisit,e.getMessage()),e);
            return BaseOutput.failure("新增失败，系统异常");
        }
    }

    @ApiOperation("更新并完成CustomerVisit")
    @ApiImplicitParams({
            @ApiImplicitParam(name="CustomerVisit", paramType="form", value = "CustomerVisit的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value="/updateAndFinished", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput updateAndFinished(CustomerVisit customerVisit) {
        try {
            customerVisit.setState(3);
            customerVisitService.updateSelective(customerVisit);
            return BaseOutput.success("更新成功");
        }catch (Exception e){
            logger.error(String.format("回访信息[%s] 更新失败[%s]",customerVisit,e.getMessage()),e);
            return BaseOutput.failure("更新失败，系统异常");
        }
    }

    @ApiOperation("修改CustomerVisit")
    @ApiImplicitParams({
		@ApiImplicitParam(name="CustomerVisit", paramType="form", value = "CustomerVisit的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(CustomerVisit customerVisit) {
        try {
            return customerVisitService.updateSelectiveWithOutput(customerVisit);
        }catch (Exception e){
            logger.error(String.format("回访信息[%s] 更新失败[%s]",customerVisit,e.getMessage()),e);
            return BaseOutput.failure("更新失败，系统异常");
        }
        
    }

    @ApiOperation("删除CustomerVisit")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "CustomerVisit的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        try {
            return customerVisitService.deleteAndEvent(id);
        }catch (Exception e){
            logger.error(String.format("回访ID[%s] 删除失败[%s]",id,e.getMessage()),e);
            return BaseOutput.failure("删除失败，系统异常");
        }
    }

    @ApiOperation("跳转到CustomerVisit详情页面")
    @RequestMapping(value="/detail.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String detail(ModelMap modelMap, @RequestParam(name="id", required = false) Long id) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new RuntimeException("未登录");
        }
        //页面上用于展示拥有者和新增时获取拥有者id
        modelMap.put("user", userTicket);
        if (null != id) {
            CustomerVisit customerVisit = customerVisitService.get(id);
            if (customerVisit == null) {
                throw new RuntimeException("根据id[" + id + "]找不到对应客户");
            }
            if(3!=customerVisit.getState()){
                modelMap.put("edit","edit");
            }
            List<Map> list = ValueProviderUtils.buildDataByProvider(getCustomerVisitMetadata(), Lists.newArrayList(customerVisit));
            modelMap.put("customerVisit", JSONObject.toJSONString(list.get(0)));
        }
        return "customerVisit/detail";
    }

    @ApiOperation("跳转到CustomerVisit编辑页面")
    @RequestMapping(value="/edit.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String edit(ModelMap modelMap, @RequestParam(name="id", required = false) Long id) throws Exception {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null){
            throw new RuntimeException("未登录");
        }
        //页面上用于展示拥有者和新增时获取拥有者id
        modelMap.put("user", userTicket);
        if (null != id) {
            CustomerVisit customerVisit = customerVisitService.get(id);
            if (customerVisit == null) {
                throw new RuntimeException("根据id[" + id + "]找不到对应客户");
            }
            if(3==customerVisit.getState()){
                modelMap.put("view","view");
            }
            List<Map> list = ValueProviderUtils.buildDataByProvider(getCustomerVisitMetadata(), Lists.newArrayList(customerVisit));
            modelMap.put("customerVisit", JSONObject.toJSONString(list.get(0)));
        }
        return "customerVisit/edit";
    }

    /**
     * 由于无法获取到表头上的meta信息，展示客户详情只有id参数，所以需要在后台构建
     * @return
     */
    private Map getCustomerVisitMetadata(){
        Map<Object, Object> metadata = new HashMap<>();
        //创建者
        JSONObject createdProvider = new JSONObject();
        createdProvider.put("provider", "ownerProvider");
        metadata.put("createdId", createdProvider);
        //回访人
        JSONObject userProvider = new JSONObject();
        userProvider.put("provider", "ownerProvider");
        metadata.put("userId", userProvider);
        //回访对象
        JSONObject customerProvider = new JSONObject();
        customerProvider.put("provider", "customerProvider");
        metadata.put("customerId", customerProvider);
        //回访状态
        JSONObject visitStateProvider = new JSONObject();
        visitStateProvider.put("provider", "visitStateProvider");
        metadata.put("state", visitStateProvider);
        //回访方式
        metadata.put("mode", getDDProvider(11L));
        //优先级
        metadata.put("priority", getDDProvider(10L));
        //完成时间
        metadata.put("finishTime", getDatetimeProvider());
        return metadata;
    }

    //获取数据字典提供者
    private JSONObject getDDProvider(Long ddId){
        JSONObject dataDictionaryValueProvider = new JSONObject();
        dataDictionaryValueProvider.put("provider", "dataDictionaryValueProvider");
        dataDictionaryValueProvider.put("queryParams", "{dd_id:"+ddId+"}");
        return dataDictionaryValueProvider;
    }

    //获取时间提供者
    private JSONObject getDatetimeProvider(){
        JSONObject datetimeProvider = new JSONObject();
        datetimeProvider.put("provider", "datetimeProvider");
        return datetimeProvider;
    }
}