package com.dili.sysadmin.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.sysadmin.domain.dto.DataDictionaryDto;

@Restful("http://crm.dili.com:8083/crm")
public interface DataDictrionaryRPC {

	@POST("/dataDictionaryApi/getByCode")
	BaseOutput<DataDictionaryDto> findDataDictionaryByCode(@VOBody String code);
}
