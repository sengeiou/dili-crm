package com.dili.alm.rpc;

import com.dili.alm.domain.dto.DataDictionaryDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOField;

@Restful("http://alm.dili.com:8084/almadmin/dataAuth")
public interface DataAuthRpc {

	@POST("/addDataAuth")
	BaseOutput<DataDictionaryDto> addDataAuth(@VOField("dataId") String dataId, @VOField("type") String type, @VOField("name") String name);

	@POST("/deleteDataAuth")
	BaseOutput<DataDictionaryDto> deleteDataAuth(@VOField("dataId") String dataId, @VOField("type") String type);

	@POST("/updateDataAuth")
	BaseOutput<DataDictionaryDto> updateDataAuth(@VOField("dataId") String dataId, @VOField("type") String type, @VOField("name") String name);

	@POST("/addDataAuth")
	BaseOutput<DataDictionaryDto> addUserDataAuth(@VOField("userId") Long userId, @VOField("dataId") String dataId, @VOField("type") String type);

	@POST("/deleteUserDataAuth")
	BaseOutput<DataDictionaryDto> deleteUserDataAuth(@VOField("userId") Long userId, @VOField("dataId") String dataId, @VOField("type") String type);
}