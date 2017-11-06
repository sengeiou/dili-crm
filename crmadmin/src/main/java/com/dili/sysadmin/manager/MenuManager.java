package com.dili.sysadmin.manager;

import java.util.List;

import com.dili.sysadmin.domain.dto.MenuJsonDto;

/**
 * <B>Description</B> <br />
 * <B>Copyright</B> Copyright (c) 2014 www.dili7 All rights reserved. <br />
 * 本软件源代码版权归地利集团,未经许可不得任意复制与传播.<br />
 * <B>Company</B> 地利集团
 * 
 * @createTime 2014-7-3 14:24:39
 * @author template
 */
public interface MenuManager {

	public List<MenuJsonDto> findAllMenuJson();

	public Integer countAllChild(Long parentId);

	void initUserMenuUrlsInRedis(Long userId);

}
