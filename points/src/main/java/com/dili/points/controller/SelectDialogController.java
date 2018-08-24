package com.dili.points.controller;

import com.dili.points.constant.PointsConstants;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.domain.dto.UserDto;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.rpc.UserRpc;
import com.dili.points.service.FirmService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
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
	private CustomerRpc customerRpc;

	@Autowired
	private FirmService firmService;
	// ================================  用户  ====================================

	@RequestMapping(value = "/user.html", method = RequestMethod.GET)
	public String user(ModelMap modelMap, @RequestParam(name="firmCode", required = false) String firmCode) {
//		if(StringUtils.isBlank(firmCode)) {
//			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
//			if(userTicket == null){
//				throw new NotLoginException();
//			}
//			modelMap.put("firmCode", userTicket.getFirmCode());
//		}else{
		modelMap.put("firmCode", firmCode);
//		}
		return "controls/user";
	}

	@ResponseBody
	@RequestMapping(value = "/listUser.action", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<User> listUser(ModelMap modelMap, UserDto user) {
		//没有市场编码，则查询所有有权限的市场下的用户
		if(StringUtils.isBlank(user.getFirmCode())) {
			List<String> firmCodes = firmService.getCurrentUserFirmCodes();
			if(firmCodes.isEmpty()){
				return null;
			}else {
				user.setFirmCodes(firmCodes);
			}
		}else{
			user.setFirmCodes(Lists.newArrayList(user.getFirmCode()));
		}
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
		if (StringUtils.isBlank(dataUrl)){
			dataUrl = request.getContextPath()+"/selectDialog/listCustomer.action";
		}
		modelMap.put("dataUrl",dataUrl);
		modelMap.put("customerId",id);
		return "controls/customer";
	}

	@ResponseBody
	@RequestMapping(value = "/listCustomer.action", method = { RequestMethod.GET, RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String listCustomer(CustomerApiDTO customer) throws Exception {
		customer.setUserId(SessionContext.getSessionContext().getUserTicket().getId());
		//查询所有市场的客户
//		List<Firm> list = firmService.listByExample(DTOUtils.newDTO(FirmDto.class));
//		customer.setFirmCodes(list.stream().map(Firm::getCode).collect(Collectors.toList()));
		//查询所有市场的客户
		customer.setMarket(PointsConstants.ALL_MARKET);
		BaseOutput<EasyuiPageOutput> output =  this.customerRpc.listPage(customer);
		return output.isSuccess() ? output.getData().toString() : null;
	}
}
