package com.dili.sysadmin.dao;

import java.util.List;
import java.util.Map;

import com.dili.ss.base.MyMapper;
import com.dili.sysadmin.domain.DataAuth;

public interface DataAuthMapper extends MyMapper<DataAuth> {

	void refreshBackup(DataAuth da);

	List<DataAuth> findByUserId(Long userId);

	List<DataAuth> findByUserIdAndTypeAndDataId(Map<String, Object> map);

	List<DataAuth> findByRoleId(Long roleId);

	List<DataAuth> findByRoleIdAndType(Map<String, Object> map);

}