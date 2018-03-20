package com.dili.points.domain;

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
 * This file was generated on 2018-03-20 11:29:31.
 */
@Table(name = "`category`")
public interface Category extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getId();

    void setId(String id);

    @Column(name = "`parent_id`")
    @FieldDef(label="上级id", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getParentId();

    void setParentId(String parentId);

    @Column(name = "`name`")
    @FieldDef(label="品名", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`source_system`")
    @FieldDef(label="来源系统", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSourceSystem();

    void setSourceSystem(String sourceSystem);

    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);
}