package com.dili.alm.rpc;

import com.dili.alm.domain.dto.DataDictionaryDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOSingleParameter;

@Restful("http://alm.dili.com:8083/alm")
public interface DataDictionaryRPC {

	@POST("/dataDictionary")
	BaseOutput<DataDictionaryDto> findDataDictionaryByCode(@VOSingleParameter String code);
}
