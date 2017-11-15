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
 * This file was generated on 2017-11-13 17:11:21.
 */
@Table(name = "`customer`")
public interface Customer extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`parent_id`")
    @FieldDef(label="父客户")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getParentId();

    void setParentId(Long parentId);

    @Column(name = "`owner_id`")
    @FieldDef(label="所有者")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOwnerId();

    void setOwnerId(Long ownerId);

    @Column(name = "`certificate_number`")
    @FieldDef(label="证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`certificate_type`")
    @FieldDef(label="证件类型", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"certificateTypeProvider\"}")
    String getCertificateType();

    void setCertificateType(String certificateType);

    @Column(name = "`certificate_time`")
    @FieldDef(label="证件日期")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCertificateTime();

    void setCertificateTime(Date certificateTime);

    @Column(name = "`certificate_addr`")
    @FieldDef(label="证件地址", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateAddr();

    void setCertificateAddr(String certificateAddr);

    @Column(name = "`code`")
    @FieldDef(label="客户编码", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    @Like(Like.BOTH)
    String getCode();

    void setCode(String code);

    @Column(name = "`name`")
    @FieldDef(label="客户名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    @Like(Like.BOTH)
    String getName();

    void setName(String name);

    @Column(name = "`sex`")
    @FieldDef(label="性别", maxLength = 10)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"data\":[{\"text\":\"男\",\"value\":\"male\"},{\"text\":\"女\",\"value\":\"female\"},{\"text\":\"未知\",\"value\":\"unknown\"}],\"provider\":\"sexProvider\"}")
    String getSex();

    void setSex(String sex);

    @Column(name = "`nation`")
    @FieldDef(label="民族", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNation();

    void setNation(String nation);

    @Column(name = "`phone`")
    @FieldDef(label="电话", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPhone();

    void setPhone(String phone);

    @Column(name = "`organization_type`")
    @FieldDef(label="组织类型", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"organizationTypeProvider\"}")
    String getOrganizationType();

    void setOrganizationType(String organizationType);

    @Column(name = "`source`")
    @FieldDef(label="归属/来源", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSource();

    void setSource(String source);

    @Column(name = "`market`")
    @FieldDef(label="所属市场", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"marketProvider\"}")
    String getMarket();

    void setMarket(String market);

    @Column(name = "`type`")
    @FieldDef(label="客户类型", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"customerTypeProvider\"}")
    String getType();

    void setType(String type);

    @Column(name = "`profession`")
    @FieldDef(label="客户行业", maxLength = 20)
    @EditMode(editor = FieldEditor.Combo, required = false, params="{\"provider\":\"professionProvider\"}")
    String getProfession();

    void setProfession(String profession);

    @Column(name = "`operating_area`")
    @FieldDef(label="经营地区", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOperatingArea();

    void setOperatingArea(String operatingArea);

    @Column(name = "`other_title`")
    @FieldDef(label="其它头衔", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOtherTitle();

    void setOtherTitle(String otherTitle);

    @Column(name = "`main_category`")
    @FieldDef(label="主营品类", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getMainCategory();

    void setMainCategory(String mainCategory);

    @Column(name = "`notes`")
    @FieldDef(label="备注信息", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

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

    @Column(name = "`yn`")
    @FieldDef(label="是否可用")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getYn();

    void setYn(Integer yn);
}