package com.dili.crm.api;

import com.dili.crm.domain.DataDictionaryValue;
import com.dili.crm.domain.dto.DataDictionaryDto;
import com.dili.crm.service.DataDictionaryService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api("/dataDictionaryApi")
@Controller
@RequestMapping("/dataDictionaryApi")
public class DataDictionaryApi {
	@Autowired
	DataDictionaryService dataDictionaryService;

	@RequestMapping("/getByCode")
	public @ResponseBody
    BaseOutput<DataDictionaryDto> getByCode(@RequestBody String code) {
		DataDictionaryDto dto = this.dataDictionaryService.findByCode(code);
		return BaseOutput.success().setData(dto);
	}

	@RequestMapping("/listByDdId")
	public @ResponseBody
	BaseOutput<List<DataDictionaryValue>> listByDdId(@RequestBody Long ddId) {
		return BaseOutput.success().setData(this.dataDictionaryService.listByDdId(ddId));
	}
}