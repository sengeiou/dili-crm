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
 * This file was generated on 2017-11-28 14:51:19.
 */
@Table(name = "`customer_extensions`")
public interface CustomerExtensions extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`acct_id`")
    @FieldDef(label="账户id", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAcctId();

    void setAcctId(String acctId);

    @Column(name = "`customer_id`")
    @FieldDef(label="所属客户")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`acct_type`")
    @FieldDef(label="类型", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"data\":[{\"text\":\"主卡\",\"value\":\"masterCard\"},{\"text\":\"副卡\",\"value\":\"slaveCard\"},{\"text\":\"帐号\",\"value\":\"account\"}],\"provider\":\"acctTypeProvider\"}")
    String getAcctType();

    void setAcctType(String acctType);

    @Column(name = "`system`")
    @FieldDef(label="所属系统", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSystem();

    void setSystem(String system);

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