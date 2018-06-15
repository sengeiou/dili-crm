package com.dili.sysadmin.manager;

import java.util.List;

import com.dili.sysadmin.domain.DataAuth;

/**
 * <B>Description</B> <br />
 * <B>Copyright</B> Copyright (c) 2014 www.diligrp.com All rights reserved.
 * <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 地利集团
 * 
 * @createTime 2015-10-4 10:02:14
 * @author template
 */
public interface DataAuthManager {

	void refreshBackup(DataAuth da);

	void initUserDataAuthsInRedis(Long userId);

	void reloadUserDataAuth(Long userId);

	DataAuth getUserCurrentDataAuth(Long userId, String dataType);

	List<DataAuth> getUserDataAuth(Long userId);
}
