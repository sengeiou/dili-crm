package com.dili.sysadmin.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dili.sysadmin.dao.ResourceMapper;
import com.dili.sysadmin.dao.RoleMapper;
import com.dili.sysadmin.dao.UserMapper;
import com.dili.sysadmin.dao.UserRoleMapper;
import com.dili.sysadmin.domain.Resource;
import com.dili.sysadmin.domain.Role;
import com.dili.sysadmin.domain.User;
import com.dili.sysadmin.domain.UserRole;
import com.dili.sysadmin.domain.dto.UserDto;
import com.dili.sysadmin.manager.MenuManager;
import com.dili.sysadmin.manager.ResourceManager;
import com.dili.sysadmin.manager.SessionRedisManager;
import com.dili.sysadmin.manager.UserManager;
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
 * @createTime 2014-7-3 14:24:41
 * @author template
 */
@Component
public class UserManagerImpl implements UserManager {
	private final static Logger LOG = LoggerFactory.getLogger(UserManagerImpl.class);

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ResourceMapper resourceMapper;
	@Autowired
	private UserRoleMapper userRoleDao;
	@Autowired
	private ManageRedisUtil myRedisUtil;
	@Autowired
	private RoleMapper roleDao;
	@Autowired
	private SessionRedisManager sessionManager;
	@Autowired
	private MenuManager menuManager;
	@Autowired
	private ResourceManager resourceManager;

	@Override
	public List<User> findOnLineUser(User user) {
		List<User> userList = new ArrayList<>();
		if (user.getId() != null) {
			String jsonRst = myRedisUtil.get(SessionConstants.USERID_SESSION_KEY + user.getId(), String.class);
			if (jsonRst != null) {
				Map<String, String> r = JSON.parseObject(jsonRst, new TypeReference<HashMap<String, String>>() {
				});
				userList.add(JSON.parseObject(r.get("user"), User.class));
			}
		} else {
			Set<String> set = this.myRedisUtil.getRedisTemplate().keys(SessionConstants.USERID_SESSION_KEY + "*");
			List<String> queryList = new ArrayList<>(set);
			List<String> rstList = this.myRedisUtil.getRedisTemplate().opsForValue().multiGet(queryList);
			for (String json : rstList) {
				Map<String, String> r = JSON.parseObject(json, new TypeReference<HashMap<String, String>>() {
				});
				userList.add(JSON.parseObject(r.get("user"), User.class));
			}
		}
		return userList;
	}

	@Override
	public List<String> receptByUsername(String username) {
		return userMapper.receptByUsername(username);
	}

	// 没Cacheable
	public Page<User> find(User user) {
		PageHelper.startPage(user.getPage(), user.getRows());
		Page<User> page = (Page<User>) userMapper.select(user);
		return page;
	}

	// @Cacheable(value = "rc", key = "'manage:user:name:' + #userName")
	public UserDto findUserByUserName(String userName) {
		User record = new User();
		record.setUserName(userName);
		User user = this.userMapper.selectOne(record);
		UserDto model = new UserDto(user);
		List<Role> roles = this.roleDao.findByUserId(user.getId());
		model.setRoles(roles);
		return model;
	}

	public User findUserByIdIgoreDel(Long pk) {
		User record = new User();
		record.setYn(null);
		return this.userMapper.selectOne(record);
	}

	public boolean save(User user) {
		return userMapper.insert(user) > 0;
	}

	// 更新用户；修改缓存
	// @Caching(evict = {
	// @CacheEvict(value = "rc", key = "'manage:userRole:userId:' +
	// #user.getID()"),
	// @CacheEvict(value = "rc", key = "'manage:user:name:' +
	// #root.getTarget().findOne(#user.getID()).getUserName()"),
	// @CacheEvict(value = "rc", key = "'manage:user:userId:' + #user.getID()"),
	// })
	// @CacheEvict(value = "rc", key = "'manage:user:userId:' + #user.getID()")
	public Boolean update(User user) {
		return userMapper.updateByPrimaryKey(user) > 0;
	}

	// 删除用户；删除用户权限
	@CachePut(value = "rc", key = "'manage:wasteUserKey'", condition = "#root.target.getRedisManager().delUser(#id)")
	public Boolean del(Long id) {
		userMapper.deleteUserRole(id);
		return userMapper.deleteByPrimaryKey(id) > 0;
	}

	// #{root.target.getCustomRedisManager().updateUserRes(user)}
	@Override
	@CachePut(value = "rc", key = "'manage:wasteUserKey'", condition = "#root.target.getRedisManager().delUser(#user)", unless = "#root.target.getRedisManager().addUser(#user)")
	public Boolean saveUserRoles(UserDto user) {
		List<Role> roles = user.getRoles();
		if (roles != null) {
			int result = 0;
			result += this.userMapper.deleteUserRole(user.getId());
			List<UserRole> userRoles = new ArrayList<>(roles.size());
			for (Role role : roles) {
				UserRole userRole = new UserRole();
				userRole.setUserId(user.getId());
				userRole.setRoleId(role.getId());
				userRoles.add(userRole);
			}
			result += this.userRoleDao.insertList(userRoles);
			return result > 0;
		}
		return false;
	}

	@Override
	// @Cacheable(value = "rc", key = "'manage:userRole:userId:' + #pk") //
	// java对象缓存
	public List<UserRole> findRolesByUserId(Long userId) {
		UserRole record = new UserRole();
		record.setUserId(userId);
		return userRoleDao.select(record);
	}

	@Override
	public List<User> findUserByRole(Long id) {
		return userMapper.findUserByRole(id);
	}

	@Override
	public boolean checkUserNumber(String serialNumber, Long id) {
		if (id != null) {
			User user = this.userMapper.selectByPrimaryKey(id);
			if (serialNumber.equals(user.getSerialNumber())) {
				return true;
			}
		}
		User record = new User();
		record.setSerialNumber(serialNumber);
		List<User> userList = this.userMapper.select(record);
		return userList.size() <= 0;
	}

	@Override
	public User findOne(Long id) {
		return this.userMapper.selectByPrimaryKey(id);
	}

	@Override
	public void clearSession(String sessionId) {
		LOG.debug("--- Clear User SessionData --- : SessionId - " + sessionId);
		this.myRedisUtil.remove(SessionConstants.SESSION_KEY_PREFIX + sessionId);
		this.sessionManager.clearUserIdSessionDataKey(this.sessionManager.getSessionUserIdKey(sessionId));
		this.sessionManager.clearSessionUserIdKey(sessionId);
	}

	@Override
	public String clearUserSession(Long userId) {
		String jsonRst = this.sessionManager.getUserIdSessionDataKey(userId.toString());
		String oldSessionId = "";
		if (jsonRst != null) {
			Map<String, String> r = JSON.parseObject(jsonRst, new TypeReference<HashMap<String, String>>() {
			});

			oldSessionId = r.get("sessionId");
		}
		LOG.debug("--- Clear User SessionData --- : SessionId - " + oldSessionId);
		this.myRedisUtil.remove(SessionConstants.SESSION_KEY_PREFIX + oldSessionId);
		this.sessionManager.clearUserIdSessionDataKey(userId.toString());
		this.sessionManager.clearSessionUserIdKey(oldSessionId);
		return oldSessionId;
	}

	@Override
	public void reloadUserUrlsByMenu(Long menuId) {
		List<User> menuUsers = this.userMapper.findUserByMenu(menuId);
		Resource resourceQuery = new Resource();
		resourceQuery.setMenuId(menuId);
		int count = this.resourceMapper.selectCount(resourceQuery);
		for (User user : menuUsers) {
			this.menuManager.initUserMenuUrlsInRedis(user.getId());
			if (count > 0) {
				this.resourceManager.initUserResourceCodeInRedis(user.getId());
			}
		}
	}

}
