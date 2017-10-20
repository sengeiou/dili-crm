package com.dili.sysadmin.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.ss.retrofitful.annotation.VOField;
import com.dili.ss.retrofitful.annotation.VOSingleParameter;
import com.dili.sysadmin.domain.dto.DataDictionaryDto;

@Restful("http://electronic.sg.com/ec")
public interface DataDictrionaryRPC {

	@POST("/dataDictionary")
	BaseOutput<DataDictionaryDto> findDataDictionaryByCode(@VOSingleParameter String code);
}
