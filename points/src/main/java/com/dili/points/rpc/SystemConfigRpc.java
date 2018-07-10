package com.dili.points.rpc;

import com.dili.points.domain.SystemConfig;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOSingleParameter;

@Restful("${crm.contextPath}")
public interface SystemConfigRpc {

	@POST("/systemConfigApi/getByCode.api")
	BaseOutput<SystemConfig> getByCode(@VOSingleParameter String code) ;

}
