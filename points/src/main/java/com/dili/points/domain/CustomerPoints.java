package com.dili.points.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-03-20 11:29:30.
 */
@Table(name = "`customer_points`")
public interface CustomerPoints extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);
    

    @Column(name = "`certificate_number`")
    @FieldDef(label="证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`available`")
    @FieldDef(label="可用积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getAvailable();

    void setAvailable(Integer available);

    @Column(name = "`frozen`")
    @FieldDef(label="冻结积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getFrozen();

    void setFrozen(Integer frozen);

    @Column(name = "`total`")
    @FieldDef(label="总积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getTotal();

    void setTotal(Integer total);

    @Column(name = "`modified_id`")
    @FieldDef(label="修改人")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getModifiedId();

    void setModifiedId(Long modifiedId);

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
}