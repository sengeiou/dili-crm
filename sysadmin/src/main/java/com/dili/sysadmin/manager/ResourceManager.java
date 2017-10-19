package com.dili.sysadmin.manager;

import java.util.List;
import java.util.Map;

import com.dili.sysadmin.domain.Resource;
import com.github.pagehelper.Page;

/**
 * <B>Description</B> <br />
 * <B>Copyright</B> Copyright (c) 2014 www.dili7 All rights reserved. <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 地利集团
 * 
 * @createTime 2014-7-3 14:24:40
 * @author template
 */
public interface ResourceManager {

	public Page<Resource> find(Resource record);

	public Boolean save(Resource resource);

	public Boolean update(Resource resource);

	public Boolean del(Long id);

	public List<Resource> listAllResourceJson(Resource resource);

	public List<String> loadResourceListToCache();

	public List<Resource> findByRole(Long id);

	public boolean checkResourceUrlUnique(String url, Long id);

	public boolean checkResourceNameUnique(String resourceName, Long id);

	void initUserResourceCodeInRedis(Long userId);

}
