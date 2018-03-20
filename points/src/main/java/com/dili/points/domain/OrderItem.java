package com.dili.points.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.math.BigDecimal;
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
@Table(name = "`order_item`")
public interface OrderItem extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`order_id`")
    @FieldDef(label="订单id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOrderId();

    void setOrderId(Long orderId);

    @Column(name = "`category_id`")
    @FieldDef(label="品类id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCategoryId();

    void setCategoryId(Long categoryId);

    @Column(name = "`total_money`")
    @FieldDef(label="小计")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTotalMoney();

    void setTotalMoney(Long totalMoney);

    @Column(name = "`weight`")
    @FieldDef(label="订单项总重量")
    @EditMode(editor = FieldEditor.Text, required = false)
    BigDecimal getWeight();

    void setWeight(BigDecimal weight);

    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);
}