package com.dili.sysadmin.manager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;

import com.dili.sysadmin.dao.MenuMapper;
import com.dili.sysadmin.dao.ResourceMapper;
import com.dili.sysadmin.dao.RoleMapper;
import com.dili.sysadmin.dao.RoleMenuMapper;
import com.dili.sysadmin.dao.UserMapper;
import com.dili.sysadmin.domain.Menu;
import com.dili.sysadmin.domain.Resource;
import com.dili.sysadmin.domain.Role;
import com.dili.sysadmin.domain.RoleMenu;
import com.dili.sysadmin.domain.User;
import com.dili.sysadmin.domain.dto.MenuJsonDto;
import com.dili.sysadmin.manager.MenuManager;
import com.dili.sysadmin.manager.ResourceManager;
import com.dili.sysadmin.manager.RoleManager;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * <B>Description</B> <br />
 * <B>Copyright</B> Copyright (c) 2014 www.dili7 All rights reserved. <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 地利集团
 * 
 * @createTime 2014-7-3 14:24:41
 * @author template
 */
@Component
public class RoleManagerImpl implements RoleManager {
	private final static Logger LOG = LoggerFactory.getLogger(RoleManagerImpl.class);

	@Autowired
	private RoleMapper roleDao;
	@Autowired
	private RoleMenuMapper roleMenuMapper;
	@Autowired
	private ResourceMapper resourceDao;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private MenuManager menuManager;
	@Autowired
	private ResourceManager resourceManager;

	public Page<Role> find(Role role) {
		PageHelper.startPage(role.getPage(), role.getRows());
		return (Page<Role>) roleDao.select(role);
	}

	public Boolean save(Role role) {
		// 去重
		Role record = new Role();
		record.setRoleName(role.getRoleName());
		if (roleDao.selectCount(record) > 0) {
			return false;
		}

		return roleDao.insert(role) > 0;
	}

	// @CacheEvict(value = "rc", key = "'manage:role:roleId:' + #role.getID()")
	public Boolean update(Role role) {
		// 去重
		Role record = new Role();
		record.setRoleName(role.getRoleName());
		List<Role> r = roleDao.select(record);
		if (r.size() == 1 && role.getId() != r.get(0).getId()) {
			return false;
		}

		return roleDao.updateByPrimaryKey(role) > 0;
	}

	// @CacheEvict(value = "rc", key = "'manage:role:roleId:' + #role.getID()")
	public Boolean del(Long id) {
		if (canBeDelete(id)) {
			roleDao.deleteByPrimaryKey(id);

			Role role = new Role();
			role.setId(id);
			return true;
		}
		return false;
	}

	// TODO：可以添加缓存到：roleDao.findUserIdByRoleId(roleId)
	private boolean canBeDelete(Long roleId) {
		return userMapper.countByRoleId(roleId) <= 0;
	}

	// @Cacheable(value = "rc", key = "'manage:menuRole:menuId' + #menuId",
	// condition = "#")
	public List<Role> findByMenu(Long menuId) {
		return roleDao.findByMenuId(menuId);
	}

	@Override
	public Boolean saveRoleMenu(Long roleId, Long[] menuIds) {
		RoleMenu record = new RoleMenu();
		record.setRoleId(roleId);
		this.roleMenuMapper.delete(record);
		int result = 0;
		for (Long menuId : menuIds) {
			record.setMenuId(menuId);
			result += this.roleMenuMapper.insert(record);
		}
		return result > 0;
	}

	// #root.getTarget().getCustomRedisManager().updateUserRes(#roleId,
	// #resourceIds)
	@SuppressWarnings("unchecked")
	@Override
	@CachePut(value = "rc", key = "'manage:wasteRoleKey'", unless = "#root.target.getRedisManager().updateRole(#roleId, #resourceIds)")
	public Boolean saveRoleResource(Long roleId, Long[] resourceIds) {
		List<Long> currentResourceIds = getCurrentResourceIdsWithRole(roleId);
		List<Long> newResourceIds = Arrays.asList(resourceIds);
		List<Long> intersection = ListUtils.retainAll(currentResourceIds, newResourceIds);
		List<Long> resourcesToBeDelete = ListUtils.subtract(currentResourceIds, intersection);
		Map<String, Object> map = new HashMap<>();
		map.put("roleId", roleId);
		map.put("resourceIds", resourceIds);
		if (!resourcesToBeDelete.isEmpty())
			roleDao.roleResourceBatchDelete(map);
		List<Long> resourcesToBeInsert = ListUtils.subtract(newResourceIds, intersection);
		if (!resourcesToBeInsert.isEmpty())
			roleDao.roleResourceBatchInsert(roleId, resourcesToBeInsert);
		return true;
	}

	@Override
	@CachePut(value = "rc", key = "'manage:wasteUserKey'", condition = "#root.target.getRedisManager().delUser(#userId)", unless = "#root.target.getRedisManager().addUser(#userId)")
	public void delUser(Long roleId, Long userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("roleId", roleId);
		map.put("userId", userId);
		roleDao.delUser(map);
	}

	@Override
	public List<Role> findByResource(Long id) {
		return roleDao.findByResource(id);
	}

	// @Cacheable(value = "rc", key = "'manage:roleResIds:roleId' + #roleId")
	public List<Long> getCurrentResourceIdsWithRole(Long roleId) {
		List<Long> resourceIds = new ArrayList<Long>();
		List<Resource> resources = resourceDao.findByRole(roleId);
		for (Resource resource : resources) {
			resourceIds.add(resource.getId());
		}
		return resourceIds;
	}

	@Override
	public List<MenuJsonDto> getRoleMenuAndResources(Long roleId) {
		List<Menu> menus = this.menuMapper.findByRoleId(roleId);
		List<MenuJsonDto> vos = new ArrayList<>(menus.size());
		BeanCopier copier = BeanCopier.create(Menu.class, MenuJsonDto.class, false);
		for (Menu menu : menus) {
			MenuJsonDto vo = new MenuJsonDto();
			copier.copy(menu, vo, null);
			if (StringUtils.isNotBlank(menu.getMenuUrl())) {
				Resource record = new Resource();
				record.setMenuId(menu.getId());
				Resource resource = this.resourceDao.selectOne(record);
				vo.setResourceId(resource.getId());
			}
			vos.add(vo);
		}
		return vos;
	}

	@Override
	public void reloadRoleUserPermission(Long roleId) {
		List<User> roleUsers = this.userMapper.findUserByRole(roleId);
		if (CollectionUtils.isEmpty(roleUsers)) {
			return;
		}
		for (User user : roleUsers) {
			// 加载用户url
			this.menuManager.initUserMenuUrlsInRedis(user.getId());
			// 加载用户resource
			this.resourceManager.initUserResourceCodeInRedis(user.getId());
		}
	}

}
