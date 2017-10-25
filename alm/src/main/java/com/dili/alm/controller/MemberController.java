package com.dili.alm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.alm.domain.User;
import com.dili.alm.rpc.UserRpc;
import com.dili.ss.domain.BaseOutput;

@RequestMapping("/member")
@Controller
public class MemberController {

	@Autowired
	private UserRpc userRPC;

	@RequestMapping(value = "/members", method = RequestMethod.GET)
	public String members(ModelMap modelMap) {
		BaseOutput<List<User>> output = this.userRPC.list(new User());
		if (output.isSuccess()) {
			modelMap.addAttribute("members", output.getData());
		}
		return "member/members";
	}

	@ResponseBody
	@RequestMapping(value = "/members", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<User> membersJson(ModelMap modelMap) {
		BaseOutput<List<User>> output = this.userRPC.list(new User());
		if (output.isSuccess()) {
			return output.getData();
		}
		return null;
	}
}
