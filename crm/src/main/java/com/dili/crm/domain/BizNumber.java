package com.dili.crm.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-12-08 09:00:40.
 */
@Table(name = "`biz_number`")
public interface BizNumber extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`type`")
    @FieldDef(label="type", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getType();

    void setType(String type);

    @Column(name = "`value`")
    @FieldDef(label="value")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getValue();

    void setValue(Long value);

    @Column(name = "`memo`")
    @FieldDef(label="memo", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getMemo();

    void setMemo(String memo);

    @Column(name = "`version`")
    @FieldDef(label="version", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getVersion();

    void setVersion(String version);
}