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
@Table(name = "`points_exchange_record`")
public interface PointsExchangeRecord extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`customer_id`")
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`points`")
    @FieldDef(label="使用积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getPoints();

    void setPoints(Integer points);

    @Column(name = "`exchange_commodities_id`")
    @FieldDef(label="兑换商品")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getExchangeCommoditiesId();

    void setExchangeCommoditiesId(Long exchangeCommoditiesId);

    @Column(name = "`quantity`")
    @FieldDef(label="兑换数量")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getQuantity();

    void setQuantity(Integer quantity);

    @Column(name = "`created`")
    @FieldDef(label="兑换时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`created_id`")
    @FieldDef(label="兑换操作员")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatedId();

    void setCreatedId(Long createdId);
}