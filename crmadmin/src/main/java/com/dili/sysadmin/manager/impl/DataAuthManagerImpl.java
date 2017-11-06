package com.dili.sysadmin.manager.impl;

import com.alibaba.fastjson.JSON;
import com.dili.sysadmin.dao.DataAuthMapper;
import com.dili.sysadmin.domain.DataAuth;
import com.dili.sysadmin.manager.DataAuthManager;
import com.dili.sysadmin.sdk.session.SessionConstants;
import com.dili.sysadmin.sdk.util.ManageRedisUtil;
import com.github.pagehelper.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
@Component
public class DataAuthManagerImpl implements DataAuthManager {
	private final static Logger LOG = LoggerFactory.getLogger(DataAuthManagerImpl.class);

	@Autowired
	private DataAuthMapper dataAuthDao;
	@Autowired
	private ManageRedisUtil redisUtil;

	@Override
	public void refreshBackup(DataAuth da) {
		dataAuthDao.refreshBackup(da);
	}

	@Override
	public void initUserDataAuthsInRedis(Long userId) {
		List<DataAuth> dataAuths = this.dataAuthDao.findByUserId(userId);
		String key = SessionConstants.USER_CURRENT_KEY + userId;
		this.redisUtil.remove(key);
		@SuppressWarnings("unchecked")
		BoundSetOperations<String, Object> ops = this.redisUtil.getRedisTemplate().boundSetOps(key);
		for (DataAuth dataAuth : dataAuths) {
			ops.add(JSON.toJSONString(dataAuth));
		}
	}

	@Override
	public void reloadUserDataAuth(Long userId) {
		String key = SessionConstants.USER_CURRENT_KEY + userId;
		this.redisUtil.remove(key);
		this.initUserDataAuthsInRedis(userId);
	}

	@Override
	public DataAuth getUserCurrentDataAuth(Long userId, String dataType) {
		DataAuth currentDataAuth = null;
		String key = SessionConstants.USER_CURRENT_KEY + userId + ":" + dataType;
		String json = this.redisUtil.get(key, String.class);
		if (StringUtil.isEmpty(json)) {
			List<DataAuth> dataAuths = this.dataAuthDao.findByUserId(userId);
			if (CollectionUtils.isEmpty(dataAuths)) {
				return null;
			}
			currentDataAuth = dataAuths.get(0);
			this.redisUtil.set(key, JSON.toJSONString(currentDataAuth));
		}
		currentDataAuth = JSON.parseObject(json, DataAuth.class);
		return currentDataAuth;
	}

	@Override
	public List<DataAuth> getUserDataAuth(Long userId) {
		return this.dataAuthDao.findByUserId(userId);
	}

}