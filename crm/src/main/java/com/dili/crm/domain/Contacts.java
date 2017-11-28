package com.dili.crm.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-11-28 14:49:44.
 */
@Table(name = "`contacts`")
public interface Contacts extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`customer_id`")
    @FieldDef(label="所属客户")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`name`")
    @FieldDef(label="姓名", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`sex`")
    @FieldDef(label="性别", maxLength = 10)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"sexProvider\"}")
    String getSex();

    void setSex(String sex);

    @Column(name = "`phone`")
    @FieldDef(label="电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPhone();

    void setPhone(String phone);

    @Column(name = "`address`")
    @FieldDef(label="地址", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddress();

    void setAddress(String address);

    @Column(name = "`position`")
    @FieldDef(label="职务/关系", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPosition();

    void setPosition(String position);

    @Column(name = "`birthday`")
    @FieldDef(label="出生日期")
    @EditMode(editor = FieldEditor.Date, required = false)
    Date getBirthday();

    void setBirthday(Date birthday);

    @Column(name = "`nation`")
    @FieldDef(label="民族", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNation();

    void setNation(String nation);

    @Column(name = "`notes`")
    @FieldDef(label="备注", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

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
}