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
 * This file was generated on 2017-09-06 15:02:40.
 */
@Table(name = "`data_dictionary_value`")
public interface DataDictionaryValue extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`dd_id`")
    @FieldDef(label="ddId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDdId();

    void setDdId(Long ddId);

    @Column(name = "`parent_id`")
    @FieldDef(label="parentId")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getParentId();

    void setParentId(Long parentId);

    @Column(name = "`order_number`")
    @FieldDef(label="orderNumber")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getOrderNumber();

    void setOrderNumber(Integer orderNumber);

    @Column(name = "`code`")
    @FieldDef(label="code", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();

    void setCode(String code);

    @Column(name = "`value`")
    @FieldDef(label="value", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getValue();

    void setValue(String value);

    @Column(name = "`notes`")
    @FieldDef(label="notes", maxLength = 1000)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

    @Column(name = "`period_begin`")
    @FieldDef(label="periodBegin")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getPeriodBegin();

    void setPeriodBegin(Date periodBegin);

    @Column(name = "`period_end`")
    @FieldDef(label="periodEnd")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getPeriodEnd();

    void setPeriodEnd(Date periodEnd);

    @Column(name = "`created`")
    @FieldDef(label="created")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`modified`")
    @FieldDef(label="modified")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);

    @Column(name = "`yn`")
    @FieldDef(label="yn")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getYn();

    void setYn(Integer yn);
}