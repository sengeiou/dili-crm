package com.dili.crm.domain;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-11-14 15:11:53.
 */
@Table(name = "`city`")
public interface City extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`CREATED`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`MODIFIED`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);

    @Column(name = "`name`")
    @FieldDef(label="名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`parent_id`")
    @FieldDef(label="父ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getParentId();

    void setParentId(Long parentId);

    @Column(name = "`short_name`")
    @FieldDef(label="简称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getShortName();

    void setShortName(String shortName);

    @Column(name = "`level_type`")
    @FieldDef(label="级别")
    @EditMode(editor = FieldEditor.Text, required = false)
    Byte getLevelType();

    void setLevelType(Byte levelType);

    @Column(name = "`city_code`")
    @FieldDef(label="区号", maxLength = 10)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCityCode();

    void setCityCode(String cityCode);

    @Column(name = "`merger_name`")
    @FieldDef(label="合并名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    @Like(Like.BOTH)
    String getMergerName();

    void setMergerName(String mergerName);

    @Column(name = "`lng`")
    @FieldDef(label="lng", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getLng();

    void setLng(String lng);

    @Column(name = "`lat`")
    @FieldDef(label="lat", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getLat();

    void setLat(String lat);

    @Column(name = "`pinyin`")
    @FieldDef(label="拼音", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPinyin();

    void setPinyin(String pinyin);

    @Column(name = "`short_py`")
    @FieldDef(label="拼音简写", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getShortPy();

    void setShortPy(String shortPy);

    @Column(name = "`yn`")
    @FieldDef(label="是否有效（1：有效 -1：无效）")
    @EditMode(editor = FieldEditor.Number, required = true)
    Boolean getYn();

    void setYn(Boolean yn);
}