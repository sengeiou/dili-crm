package com.dili.points.domain;

import com.dili.ss.domain.annotation.Like;
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
@Table(name = "`exchange_commodities`")
public interface ExchangeCommodities extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`name`")
    @FieldDef(label="商品名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    @Like
    String getName();

    void setName(String name);

    @Column(name = "`points`")
    @FieldDef(label="售价（积分）")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getPoints();

    void setPoints(Integer points);

    @Column(name = "`available`")
    @FieldDef(label="可兑换")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getAvailable();

    void setAvailable(Integer available);

    @Column(name = "`total`")
    @FieldDef(label="总量")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getTotal();

    void setTotal(Integer total);

    @Column(name = "`created`")
    @FieldDef(label="操作时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`created_id`")
    @FieldDef(label="操作员")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatedId();

    void setCreatedId(Long createdId);

    @Column(name = "`modified`")
    @FieldDef(label="操作时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);

    @Column(name = "`modified_id`")
    @FieldDef(label="操作员")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getModifiedId();

    void setModifiedId(Long modifiedId);
}