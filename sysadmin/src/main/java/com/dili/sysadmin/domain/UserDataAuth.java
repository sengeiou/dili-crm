package com.dili.sysadmin.domain;

import com.dili.ss.domain.BaseDomain;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-07-17 17:00:12.
 */
@Table(name = "`user_data_auth`")
public class UserDataAuth extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 数据权限表id
     */
    @Column(name = "`data_auth_id`")
    private Long dataAuthId;

    /**
     * 用户id
     */
    @Column(name = "`user_id`")
    private Long userId;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取数据权限表id
     *
     * @return data_auth_id - 数据权限表id
     */
    public Long getDataAuthId() {
        return dataAuthId;
    }

    /**
     * 设置数据权限表id
     *
     * @param dataAuthId 数据权限表id
     */
    public void setDataAuthId(Long dataAuthId) {
        this.dataAuthId = dataAuthId;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}