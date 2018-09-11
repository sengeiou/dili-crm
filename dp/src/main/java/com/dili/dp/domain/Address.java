package com.dili.dp.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-11-15 11:16:13.
 */
@Table(name = "`address`")
public interface Address extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`customer_id`")
    @FieldDef(label="客户")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`name`")
    @FieldDef(label="名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`address`")
    @FieldDef(label="地址", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddress();

    void setAddress(String address);

    @Column(name = "`city_id`")
    @FieldDef(label="所在城市", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCityId();

    void setCityId(String cityId);

    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`modified`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);

    @Column(name = "`created_id`")
    @FieldDef(label="创建人")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatedId();

    void setCreatedId(Long createdId);

    @Column(name = "`modified_id`")
    @FieldDef(label="修改人")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getModifiedId();

    void setModifiedId(Long modifiedId);

    @Column(name = "`lat`")
    @FieldDef(label="纬度")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getLat();

    void  setLat(String lat);

    @Column(name = "`lng`")
    @FieldDef(label="经度")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getLng();

    void  setLng(String lng);

    @Column(name = "`is_default`")
    @FieldDef(label="是否默认地址")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getIsDefault();

    void setIsDefault(Integer isDefault);
}