package com.dili.crm.controller;

import com.dili.crm.domain.User;
import com.dili.crm.rpc.UserRpc;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 选择对话框控制器
 */
@RequestMapping("/selectDialog")
@Controller
public class SelectDialogController {

	@Autowired
	private UserRpc userRPC;

	@RequestMapping(value = "/user.html", method = RequestMethod.GET)
	public String members(ModelMap modelMap, @RequestParam("textboxId") String textboxId) {
		modelMap.put("textboxId", textboxId);
		return "controls/user";
	}

	@ResponseBody
	@RequestMapping(value = "/listUser", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<User> list(ModelMap modelMap, User user) {
		BaseOutput<List<User>> output = this.userRPC.listByExample(user);
		if (output.isSuccess()) {
			return output.getData();
		}
		return null;
	}
}
