package com.dili.sysadmin.domain;

import com.dili.ss.domain.BaseDomain;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-07-10 17:58:29.
 */
@Table(name = "`role_menu`")
public class RoleMenu extends BaseDomain {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色ID##外键
     */
    @Column(name = "`role_id`")
    private Long roleId;

    /**
     * 菜单ID##外键
     */
    @Column(name = "`menu_id`")
    private Long menuId;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取角色ID##外键
     *
     * @return role_id - 角色ID##外键
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 设置角色ID##外键
     *
     * @param roleId 角色ID##外键
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取菜单ID##外键
     *
     * @return menu_id - 菜单ID##外键
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * 设置菜单ID##外键
     *
     * @param menuId 菜单ID##外键
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}