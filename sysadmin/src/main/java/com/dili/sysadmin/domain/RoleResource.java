package com.dili.sysadmin.domain;

import com.dili.ss.domain.BaseDomain;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-07-20 15:26:11.
 */
@Table(name = "`role_resource`")
public class RoleResource extends BaseDomain {
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
     * 资源ID##外键
     */
    @Column(name = "`resource_id`")
    private Long resourceId;

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
     * 获取资源ID##外键
     *
     * @return resource_id - 资源ID##外键
     */
    public Long getResourceId() {
        return resourceId;
    }

    /**
     * 设置资源ID##外键
     *
     * @param resourceId 资源ID##外键
     */
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}