package com.dili.sysadmin.dao;

import com.dili.ss.base.MyMapper;
import com.dili.sysadmin.domain.Menu;
import com.dili.sysadmin.domain.dto.MenuDto;
import com.dili.sysadmin.domain.dto.MenuJsonDto;

import java.util.List;

public interface MenuMapper extends MyMapper<Menu> {

	List<Menu> findByUserId(Long id);

	List<Menu> findByRoleId(Long id);

	List<MenuJsonDto> findAllMenuJson();

	List<MenuDto> selectMenuDto();

	List<MenuDto> selectRoleMenuDto(Long roleId);

	String getParentMenus(String id);
}