package com.dili.sysadmin.manager;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.dili.sysadmin.domain.Role;
import com.dili.sysadmin.domain.dto.MenuJsonDto;
import com.github.pagehelper.Page;

/**
 * <B>Description</B> <br />
 * <B>Copyright</B> Copyright (c) 2014 www.dili7 All rights reserved. <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 地利集团
 * 
 * @createTime 2014-7-3 14:24:41
 * @author template
 */
public interface RoleManager {

	public Page<Role> find(Role role);

	public Boolean save(Role role);

	public Boolean update(Role role);

	public Boolean del(Long id);

	public Boolean saveRoleMenu(Long roleId, Long[] menus);

	public List<Role> findByMenu(Long menuId);

	public Boolean saveRoleResource(Long roleId, Long[] resourceIds);

	public List<Long> getCurrentResourceIdsWithRole(Long roleId);

	public void delUser(Long roleId, Long userId);

	public List<Role> findByResource(Long id);

	public List<MenuJsonDto> getRoleMenuAndResources(Long roleId);

	@Async
	public void reloadRoleUserPermission(Long roleId);
}
