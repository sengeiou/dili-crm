package com.dili.points.rpc;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.uap.sdk.domain.UserDataAuth;

import java.util.List;
import java.util.Map;

/**
 * 用户数据权限服务
 * Created by asiamaster on 2017/10/19 0019.
 */
@Restful("${uap.contextPath}")
public interface DataAuthRpc {

	/**
     * 根据条件查询用户数据权限表信息
	 * @param userDataAuth	用户id必填
	 * @return UserDataAuth列表
	 */
	@POST("/dataAuthApi/listUserDataAuth.api")
	BaseOutput<List<UserDataAuth>> listUserDataAuth(@VOBody UserDataAuth userDataAuth);

	/**
	 * 根据条件查询用户数据权限value列表
	 * @param userDataAuth userId和refCode必填
	 * @return
	 */
	@POST("/dataAuthApi/listUserDataAuthValues.api")
	BaseOutput<List<String>> listUserDataAuthValues(@VOBody UserDataAuth userDataAuth);

	/**
	 * 根据条件查询用户数据权限
	 * @param userDataAuth userId和refCode必填
	 * @return Map key为value, 值为转义后的行数据
	 */
	@POST("/dataAuthApi/listUserDataAuthDetail.api")
	BaseOutput<List<Map>> listUserDataAuthDetail(@VOBody UserDataAuth userDataAuth);
}
