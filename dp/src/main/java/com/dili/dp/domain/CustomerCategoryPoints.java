package com.dili.dp.domain;

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
 * This file was generated on 2018-05-09 17:35:45.
 */
@Table(name = "`customer_category_points`")
public interface CustomerCategoryPoints extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`customer_id`")
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = true)
	Long getCustomerId();
	void setCustomerId(Long customerId);
	
    @Column(name = "`name`")
    @FieldDef(label="客户名称", maxLength = 40)
    @Like(value = Like.BOTH)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`certificate_number`")
    @FieldDef(label="证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`certificate_type`")
    @FieldDef(label="证件类型", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"dataDictionaryValueProvider\",\"queryParams\":{\"dd_id\":3}}")
    String getCertificateType();

    void setCertificateType(String certificateType);

    @Column(name = "`organization_type`")
    @FieldDef(label="组织类型", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"dataDictionaryValueProvider\",\"queryParams\":{\"dd_id\":5}}")
    String getOrganizationType();

    void setOrganizationType(String organizationType);

    @Column(name = "`available`")
    @FieldDef(label="累计可用积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getAvailable();

    void setAvailable(Integer available);

    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`buyer_points`")
    @FieldDef(label="买家积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getBuyerPoints();

    void setBuyerPoints(Integer buyerPoints);

    @Column(name = "`seller_points`")
    @FieldDef(label="卖家积分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSellerPoints();

    void setSellerPoints(Integer sellerPoints);

    @Column(name = "`source_system`")
    @FieldDef(label="来源系统", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSourceSystem();

    void setSourceSystem(String sourceSystem);

    @Column(name = "`category1_id`")
    @FieldDef(label="一级品类id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCategory1Id();

    void setCategory1Id(Long category1Id);

    @Column(name = "`category1_name`")
    @FieldDef(label="一级品类名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCategory1Name();

    void setCategory1Name(String category1Name);

    @Column(name = "`category2_id`")
    @FieldDef(label="二级品类id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCategory2Id();

    void setCategory2Id(Long category2Id);

    @Column(name = "`category2_name`")
    @FieldDef(label="二级品类名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCategory2Name();

    void setCategory2Name(String category2Name);

    @Column(name = "`category3_id`")
    @FieldDef(label="三级品类id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCategory3Id();

    void setCategory3Id(Long category3Id);

    @Column(name = "`category3_name`")
    @FieldDef(label="三级品类名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCategory3Name();

    void setCategory3Name(String category3Name);
    
    @Column(name = "`firm_code`")
    @FieldDef(label="市场编码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getFirmCode();
    void setFirmCode(String firmCode);
}