package com.dili.points.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.ss.retrofitful.annotation.VOSingleParameter;
import com.dili.uap.sdk.domain.User;

import java.util.List;

/**
 * Created by asiamaster on 2017/10/19 0019.
 */
@Restful("${uap.contextPath}")
public interface UserRpc {

	@POST("/userApi/get.api")
	BaseOutput<User> get(@VOSingleParameter Long id);

	@POST("/userApi/list.api")
	BaseOutput<List<User>> list(@VOBody User user);

	@POST("/userApi/listByExample.api")
	BaseOutput<List<User>> listByExample(@VOBody User user);

	@POST("/userApi/listUserByIds.api")
	BaseOutput<List<User>> listUserByIds(@VOSingleParameter List<String> ids);

}
