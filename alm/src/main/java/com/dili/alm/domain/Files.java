package com.dili.alm.domain;

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
 * This file was generated on 2017-10-23 11:17:08.
 */
@Table(name = "`files`")
public interface Files extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`name`")
    @FieldDef(label="文件名", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`suffix`")
    @FieldDef(label="文件后缀", maxLength = 10)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSuffix();

    void setSuffix(String suffix);

    @Column(name = "`length`")
    @FieldDef(label="文件大小")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getLength();

    void setLength(Long length);

    @Column(name = "`url`")
    @FieldDef(label="文件地址", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getUrl();

    void setUrl(String url);

    @Column(name = "`milestones_id`")
    @FieldDef(label="里程碑id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMilestonesId();

    void setMilestonesId(Long milestonesId);

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

    @Column(name = "`create_member_id`")
    @FieldDef(label="创建人id")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"memberProvider\"}")
    Long getCreateMemberId();

    void setCreateMemberId(Long createMemberId);

    @Column(name = "`modify_member_id`")
    @FieldDef(label="修改人id")
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"memberProvider\"}")
    Long getModifyMemberId();

    void setModifyMemberId(Long modifyMemberId);
}