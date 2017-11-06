package com.dili.sysadmin.service;

import java.util.List;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.sysadmin.domain.DataAuth;
import com.dili.sysadmin.domain.dto.DataAuthTypeDto;
import com.dili.sysadmin.domain.dto.EditRoleDataAuthDto;
import com.dili.sysadmin.domain.dto.EditUserDataAuthDto;
import com.dili.sysadmin.domain.dto.UpdateRoleDataAuthDto;
import com.dili.sysadmin.domain.dto.UpdateUserDataAuthDto;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-04 15:24:51.
 */
public interface DataAuthService extends BaseService<DataAuth, Long> {

	/**
	 * 获取用户的数据权限
	 * 
	 * @return
	 */
	List<DataAuth> fetchUserDataAuth(Long userId);

	/**
	 * 修改当前数据权限
	 * 
	 * @param userId
	 * @param type
	 * @param dataId
	 */
	public void changeCurrentDataAuth(Long userId, String type, String dataId);

	/**
	 * 获取用户指定类型当前的数据权限
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	public DataAuth fetchCurrentDataAuth(Long userId, String type);

	public EditUserDataAuthDto queryEditUserDataAuth(Long userId, String type);

	BaseOutput<Object> updateUserDataAuth(UpdateUserDataAuthDto dto);

	List<DataAuthTypeDto> fetchDataAuthType();

	EditRoleDataAuthDto queryEditRoleDataAuth(Long roleId, String type);

	BaseOutput<Object> updateRoleDataAuth(UpdateRoleDataAuthDto dto);

}