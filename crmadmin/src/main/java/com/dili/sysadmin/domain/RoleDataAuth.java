package com.dili.sysadmin.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-08-29 10:13:51.
 */
@Table(name = "`role_data_auth`")
public class RoleDataAuth extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`role_id`")
    private Long roleId;

    @Column(name = "`data_auth_id`")
    private Long dataAuthId;

    /**
     * @return id
     */
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
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
     * @return role_id
     */
    @FieldDef(label="roleId")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * @return data_auth_id
     */
    @FieldDef(label="dataAuthId")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getDataAuthId() {
        return dataAuthId;
    }

    /**
     * @param dataAuthId
     */
    public void setDataAuthId(Long dataAuthId) {
        this.dataAuthId = dataAuthId;
    }
}