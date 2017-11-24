package com.dili.sysadmin.service;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.sysadmin.domain.Menu;
import com.dili.sysadmin.domain.dto.MenuListDto;
import com.dili.sysadmin.domain.dto.UpdateMenuDto;
import com.dili.sysadmin.exception.MenuException;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-04 15:24:51.
 */
public interface MenuService extends BaseService<Menu, Long> {

	List<Menu> findUserMenu(String userId) throws MenuException;

	List<MenuListDto> listContainAndParseResource();

	BaseOutput<Object> update(UpdateMenuDto dto);

	BaseOutput<Object> deleteCheckIsBinding(Long menuId);

	/**
	 * 获取上级菜单
	 * @param id
	 * @return
	 */
	List<Menu> getParentMenus(String id);

	/**
	 * 根据url获取上级菜单
	 * @param url
	 * @return
	 */
	List<Menu> getParentMenusByUrl(String url);

}