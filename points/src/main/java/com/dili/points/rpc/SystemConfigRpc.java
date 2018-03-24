package com.dili.points.rpc;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dili.points.domain.Customer;
import com.dili.points.domain.SystemConfig;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.ss.retrofitful.annotation.VOSingleParameter;

import io.swagger.annotations.Api;

@Restful("${crm.contextPath}")
public interface SystemConfigRpc {

	@POST("/systemConfigApi/getByCode")
	BaseOutput<SystemConfig> getByCode(@VOSingleParameter String code) ;

}
