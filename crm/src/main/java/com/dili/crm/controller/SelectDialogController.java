package com.dili.crm.controller;

import com.dili.crm.domain.Customer;
import com.dili.crm.domain.User;
import com.dili.crm.rpc.UserRpc;
import com.dili.crm.service.CustomerService;
import com.dili.ss.domain.BaseOutput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 选择对话框控制器
 */
@RequestMapping("/selectDialog")
@Controller
public class SelectDialogController {

	@Autowired
	private UserRpc userRPC;

	@Autowired
	private CustomerService customerService;

	// ================================  用户  ====================================

	@RequestMapping(value = "/user.html", method = RequestMethod.GET)
	public String user(ModelMap modelMap, @RequestParam("textboxId") String textboxId) {
		modelMap.put("textboxId", textboxId);
		return "controls/user";
	}

	@ResponseBody
	@RequestMapping(value = "/listUser.action", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<User> listUser(ModelMap modelMap, User user) {
		BaseOutput<List<User>> output = this.userRPC.listByExample(user);
		if (output.isSuccess()) {
			return output.getData();
		}
		return null;
	}

	// ================================  客户  ====================================

	/**
	 * 根据条件检索对应的客户信息
	 * @param modelMap
	 * @param request
	 * @param textboxId 控件ID
	 * @param dataUrl  检索数据的url
	 * @param id  客户ID(视情况而定，如有，则可能会使用，如无，则可忽略)
	 * @return
	 */
	@RequestMapping(value = "/customer.html", method = RequestMethod.GET)
	public String customer(ModelMap modelMap, HttpServletRequest request, @RequestParam(name="textboxId", required = false) String textboxId, @RequestParam(name="dataUrl", required = false) String dataUrl,@RequestParam(name="id", required = false) Long id) {
		modelMap.put("textboxId", textboxId);
		if (StringUtils.isBlank(dataUrl)){
			dataUrl = request.getContextPath()+"/selectDialog/listCustomer.action";
		}
		modelMap.put("dataUrl",dataUrl);
		modelMap.put("customerId",id);
		return "controls/customer";
	}

	@ResponseBody
	@RequestMapping(value = "/listCustomer.action", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listCustomer(ModelMap modelMap, Customer customer) throws Exception {
		return this.customerService.listEasyuiPageByExample(customer, true).toString();
	}
}
