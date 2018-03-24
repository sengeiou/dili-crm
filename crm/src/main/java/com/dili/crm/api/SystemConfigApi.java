package com.dili.crm.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dili.crm.domain.SystemConfig;
import com.dili.crm.domain.dto.DataDictionaryDto;
import com.dili.crm.service.SystemConfigService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;

import io.swagger.annotations.Api;

@Api("/systemConfigApi")
@Controller
@RequestMapping("/systemConfigApi")
public class SystemConfigApi {
	@Autowired
	SystemConfigService systemConfigService;

	@RequestMapping("/getByCode")
	public @ResponseBody
    BaseOutput<SystemConfig> getByCode(@RequestBody String code) {
		SystemConfig systemConfig = DTOUtils.newDTO(SystemConfig.class);
		systemConfig.setCode(code);
		List<SystemConfig> dto = this.systemConfigService.list(systemConfig);
		return dto.isEmpty() ? BaseOutput.success() : BaseOutput.success().setData(dto.get(0));
	}

	
}