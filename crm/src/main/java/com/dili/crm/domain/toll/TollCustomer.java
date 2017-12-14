package com.dili.crm.domain.toll;

import com.dili.ss.domain.annotation.Operator;
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
 * This file was generated on 2017-12-14 10:44:02.
 */
@Table(name = "`customer`")
public interface TollCustomer extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`name`")
    @FieldDef(label="姓名", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`remarks`")
    @FieldDef(label="备注", maxLength = 256)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getRemarks();

    void setRemarks(String remarks);

    @Column(name = "`yn`")
    @FieldDef(label="1:有效;-1:无效")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getYn();

    void setYn(Integer yn);

    @Column(name = "`CREATED`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    @Operator(Operator.GREAT_THAN)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`MODIFIED`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getModified();

    void setModified(Date modified);

    @Column(name = "`ID_card`")
    @FieldDef(label="身份证", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getIdCard();

    void setIdCard(String idCard);

    @Column(name = "`alias`")
    @FieldDef(label="别名", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAlias();

    void setAlias(String alias);

    @Column(name = "`sex`")
    @FieldDef(label="性别，1:男；2:女")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSex();

    void setSex(Long sex);

    @Column(name = "`birthdate`")
    @FieldDef(label="出生日期")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getBirthdate();

    void setBirthdate(Date birthdate);

    @Column(name = "`card_addr`")
    @FieldDef(label="户口所在地", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCardAddr();

    void setCardAddr(String cardAddr);

    @Column(name = "`type`")
    @FieldDef(label="客户类型，1：市场业户；2：其他")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getType();

    void setType(Long type);

    @Column(name = "`cellphone`")
    @FieldDef(label="手机号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCellphone();

    void setCellphone(String cellphone);

    @Column(name = "`phone`")
    @FieldDef(label="联系方式", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPhone();

    void setPhone(String phone);

    @Column(name = "`association_member`")
    @FieldDef(label="协会成员，1：非会员；2：会员")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAssociationMember();

    void setAssociationMember(Long associationMember);

    @Column(name = "`number_plate`")
    @FieldDef(label="车牌号", maxLength = 10)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNumberPlate();

    void setNumberPlate(String numberPlate);

    @Column(name = "`home_addr`")
    @FieldDef(label="家庭住址", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getHomeAddr();

    void setHomeAddr(String homeAddr);

    @Column(name = "`addr`")
    @FieldDef(label="现在住址", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddr();

    void setAddr(String addr);

    @Column(name = "`operator`")
    @FieldDef(label="操作员")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOperator();

    void setOperator(Long operator);

    @Column(name = "`operator_name`")
    @FieldDef(label="operatorName", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOperatorName();

    void setOperatorName(String operatorName);

    @Column(name = "`department_id`")
    @FieldDef(label="部门ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDepartmentId();

    void setDepartmentId(Long departmentId);

    @Column(name = "`photo`")
    @FieldDef(label="照片")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPhoto();

    void setPhoto(String photo);

    @Column(name = "`IC_info`")
    @FieldDef(label="IC卡信息")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getIcInfo();

    void setIcInfo(String icInfo);
}