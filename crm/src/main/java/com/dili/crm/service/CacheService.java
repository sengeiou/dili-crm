package com.dili.crm.service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-11-13 17:11:22.
 */
public interface CacheService {

	/**
	 * 刷新部门缓存
	 */
	void refreshDepartment();

	/**
	 * 刷新城市缓存
	 */
	void refreshCity();

	/**
	 * 强制刷新城市缓存
	 */
	void forceRefreshCity();
}