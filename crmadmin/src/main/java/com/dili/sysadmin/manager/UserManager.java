package com.dili.sysadmin.manager;

import com.dili.sysadmin.domain.User;
import com.dili.sysadmin.domain.UserRole;
import com.dili.sysadmin.domain.dto.UserDto;
import com.github.pagehelper.Page;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

/**
 * <B>Description</B> <br />
 * <B>Copyright</B> Copyright (c) 2014 www.dili7 All rights reserved. <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 地利集团
 * 
 * @createTime 2014-7-3 14:24:41
 * @author template
 */
public interface UserManager {

	public Page<User> find(User user);

	public User findOne(Long pk);

	public boolean save(User user);

	public Boolean update(User user);

	public Boolean del(Long id);

	public UserDto findUserByUserName(String userName);

	public Boolean saveUserRoles(UserDto user);

	public List<UserRole> findRolesByUserId(Long pk);

	public List<User> findUserByRole(Long id);

	public User findUserByIdIgoreDel(Long pk);

	public boolean checkUserNumber(String serialNumber, Long id);

	public List<User> findOnLineUser(User user);

	List<String> receptByUsername(String username);

	public void clearSession(String sessionId);

	public String clearUserSession(Long userId);

	@Async
	public void reloadUserUrlsByMenu(Long menuId);
}
