package com.dili.crm.domain;

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
 * This file was generated on 2017-11-30 15:47:55.
 */
@Table(name = "`icard_user_card`")
public interface IcardUserCard extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`device_id`")
    @FieldDef(label="卡片硬件标识", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getDeviceId();

    void setDeviceId(String deviceId);

    @Column(name = "`card_no`")
    @FieldDef(label="卡号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getCardNo();

    void setCardNo(String cardNo);

    @Column(name = "`category`")
    @FieldDef(label="卡类别-主/副/临时/联营")
    @EditMode(editor = FieldEditor.Text, required = true)
    Byte getCategory();

    void setCategory(Byte category);

    @Column(name = "`type`")
    @FieldDef(label="卡类型")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getType();

    void setType(Integer type);

    @Column(name = "`account_id`")
    @FieldDef(label="用户账号")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getAccountId();

    void setAccountId(Long accountId);

    @Column(name = "`status`")
    @FieldDef(label="卡片状态")
    @EditMode(editor = FieldEditor.Text, required = true)
    Byte getStatus();

    void setStatus(Byte status);

    @Column(name = "`verify_code`")
    @FieldDef(label="验证码")
    @EditMode(editor = FieldEditor.Text, required = true)
    String getVerifyCode();

    void setVerifyCode(String verifyCode);

    @Column(name = "`cash_pledge`")
    @FieldDef(label="卡片押金-分")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCashPledge();

    void setCashPledge(Long cashPledge);

    @Column(name = "`relieve_lock_time`")
    @FieldDef(label="解锁时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getRelieveLockTime();

    void setRelieveLockTime(Date relieveLockTime);

    @Column(name = "`relieve_loss_time`")
    @FieldDef(label="解挂时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getRelieveLossTime();

    void setRelieveLossTime(Date relieveLossTime);

    @Column(name = "`parent_account_id`")
    @FieldDef(label="父级用户账号-副卡 联营卡专用")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getParentAccountId();

    void setParentAccountId(Long parentAccountId);

    @Column(name = "`auth_account_id`")
    @FieldDef(label="授权账号-联营卡专用")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAuthAccountId();

    void setAuthAccountId(Long authAccountId);

    @Column(name = "`institution_code`")
    @FieldDef(label="机构编码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getInstitutionCode();

    void setInstitutionCode(String institutionCode);

    @Column(name = "`institution_name`")
    @FieldDef(label="机构名称-保留字段", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getInstitutionName();

    void setInstitutionName(String institutionName);

    @Column(name = "`user_station_id`")
    @FieldDef(label="用户工位")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getUserStationId();

    void setUserStationId(Long userStationId);

    @Column(name = "`employee_id`")
    @FieldDef(label="操作人员")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getEmployeeId();

    void setEmployeeId(Long employeeId);

    @Column(name = "`version`")
    @FieldDef(label="应用程序版本号")
    @EditMode(editor = FieldEditor.Text, required = true)
    Short getVersion();

    void setVersion(Short version);

    @Column(name = "`created_time`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getCreatedTime();

    void setCreatedTime(Date createdTime);

    @Column(name = "`modified_time`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getModifiedTime();

    void setModifiedTime(Date modifiedTime);
}