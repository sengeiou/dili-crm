package com.dili.sysadmin.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dili.sysadmin.dao.ResourceMapper;
import com.dili.sysadmin.domain.Resource;
import com.dili.sysadmin.domain.Role;
import com.dili.sysadmin.manager.ResourceManager;
import com.dili.sysadmin.manager.RoleManager;
import com.dili.sysadmin.sdk.session.SessionConstants;
import com.dili.sysadmin.sdk.util.ManageRedisUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * <B>Description</B> <br />
 * <B>Copyright</B> Copyright (c) 2014 www.dili7 All rights reserved. <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 地利集团
 * 
 * @createTime 2014-7-3 14:24:40
 * @author template
 */
@Component
public class ResourceManagerImpl implements ResourceManager {
	private final static Logger LOG = LoggerFactory.getLogger(ResourceManagerImpl.class);

	@Autowired
	private ResourceMapper resourceDao;

	@Autowired
	private ManageRedisUtil redisUtils;

	@Autowired
	private RoleManager roleManager;

	public Page<Resource> find(Resource record) {
		PageHelper.startPage(record.getPage(), record.getRows());
		Page<Resource> list = (Page<Resource>) resourceDao.select(record);
		return list;
	}

	@Override
	public List<String> loadResourceListToCache() {
		LOG.info("---Load Resource List---");
		List<Resource> res = resourceDao.selectAll();
		List<String> list = new ArrayList<>(res.size());
		redisUtils.set(SessionConstants.CACHED_RESOURCE_LIST_KEY, list);
		LOG.info(String.format("Resource List Records: %d", list.size()));
		LOG.info("---Load Resource List Successfully---");
		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Boolean save(Resource resource) {
		int result = resourceDao.insert(resource);
		redisUtils.remove(SessionConstants.CACHED_RESOURCE_LIST_KEY);
		return result > 0;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	// @CacheEvict(value = "rc", key = "'manage:res:resId:' +
	// #resource.getId()")
	public Boolean update(Resource resource) {

		int result = resourceDao.updateByPrimaryKey(resource);
		// TODO 整理一下代码，有些代码要删除了
		redisUtils.remove(SessionConstants.CACHED_RESOURCE_LIST_KEY);
		return result > 0;
	}

	@CachePut(value = "rc", key = "'manage:wasteResKey'", condition = "#root.target.getRedisManager().delRes(#id)")
	public Boolean del(Long id) {
		Resource re = this.resourceDao.selectByPrimaryKey(id);
		List<Role> roles = roleManager.findByResource(id);
		if (roles != null && roles.size() > 0) {
			StringBuffer sb = new StringBuffer();
			Boolean b = false;
			for (Role r : roles) {
				if (b) {
					sb.append(",");
				}
				b = true;
				sb.append(r.getRoleName());
			}
			throw new RuntimeException("[" + sb.toString() + "]引用了权限[" + re.getName() + "]！无法删除！");
		}

		int result = resourceDao.deleteByPrimaryKey(id);
		// TODO 整理一下代码，有些代码要删除了
		redisUtils.remove(SessionConstants.CACHED_RESOURCE_LIST_KEY);
		return result > 0;
	}

	@Override
	// @Cacheable(value = "rc", key = "'manage:resJsonAll'")
	public List<Resource> listAllResourceJson(Resource resource) {
		return resourceDao.listAllResourceJson(resource);
	}

	@Override
	// @Cacheable(value = "rc", key = "'manage:roleResAll:roleId' + #id")
	public List<Resource> findByRole(Long id) {
		return resourceDao.findByRole(id);
	}

	@Override
	public boolean checkResourceUrlUnique(String url, Long id) {
		return resourceDao.checkResourceUrlUnique(url, id);
	}

	@Override
	public boolean checkResourceNameUnique(String resourceName, Long id) {
		return resourceDao.checkResourceNameUnique(resourceName, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initUserResourceCodeInRedis(Long userId) {
		List<Resource> resources = this.resourceDao.findByUserId(userId);
		if (CollectionUtils.isEmpty(resources)) {
			return;
		}
		List<String> codes = new ArrayList<>(resources.size());
		for (Resource resource : resources) {
			if (StringUtils.isNotBlank(resource.getCode())) {
				codes.add(resource.getCode());
			}
		}
		if (CollectionUtils.isEmpty(codes)) {
			return;
		}
		String key = SessionConstants.USER_RESOURCE_CODE_KEY + userId;
		this.redisUtils.remove(key);
		this.redisUtils.getRedisTemplate().boundSetOps(key).add(codes.toArray());
	}
}
