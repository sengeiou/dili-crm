package com.dili.sysadmin.domain;

import com.dili.ss.domain.BaseDomain;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-07-10 17:58:29.
 */
@Table(name = "`user_role`")
public class UserRole extends BaseDomain {
    /**
     * 主键
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id##外键
     */
    @Column(name = "`user_id`")
    private Long userId;

    /**
     * 角色id##外键
     */
    @Column(name = "`role_id`")
    private Long roleId;

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
     * 获取用户id##外键
     *
     * @return user_id - 用户id##外键
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户id##外键
     *
     * @param userId 用户id##外键
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取角色id##外键
     *
     * @return role_id - 角色id##外键
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 设置角色id##外键
     *
     * @param roleId 角色id##外键
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}