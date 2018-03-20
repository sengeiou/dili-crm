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
@Table(name = "`order`")
public interface Order extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`code`")
    @FieldDef(label="订单编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();

    void setCode(String code);

    @Column(name = "`seller_id`")
    @FieldDef(label="卖家身份证号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSellerId();

    void setSellerId(String sellerId);

    @Column(name = "`seller_card_no`")
    @FieldDef(label="卖家卡号")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSellerCardNo();

    void setSellerCardNo(Long sellerCardNo);

    @Column(name = "`buyer_id`")
    @FieldDef(label="买家身份证号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getBuyerId();

    void setBuyerId(String buyerId);

    @Column(name = "`buyer_card_no`")
    @FieldDef(label="买家卡号")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getBuyerCardNo();

    void setBuyerCardNo(Long buyerCardNo);

    @Column(name = "`market`")
    @FieldDef(label="所属市场", maxLength = 50)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"dataDictionaryValueProvider\",\"queryParams\":{\"dd_id\":2}}")
    String getMarket();

    void setMarket(String market);

    @Column(name = "`total_money`")
    @FieldDef(label="总价")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTotalMoney();

    void setTotalMoney(Long totalMoney);

    @Column(name = "`weight`")
    @FieldDef(label="订单总重量")
    @EditMode(editor = FieldEditor.Text, required = false)
    BigDecimal getWeight();

    void setWeight(BigDecimal weight);

    @Column(name = "`pay_date`")
    @FieldDef(label="付款日期")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getPayDate();

    void setPayDate(Date payDate);

    @Column(name = "`payment`")
    @FieldDef(label="支付方式")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getPayment();

    void setPayment(Integer payment);

    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`business_type`")
    @FieldDef(label="业务类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getBusinessType();

    void setBusinessType(Integer businessType);

    @Column(name = "`source_system`")
    @FieldDef(label="来源系统", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSourceSystem();

    void setSourceSystem(String sourceSystem);
}