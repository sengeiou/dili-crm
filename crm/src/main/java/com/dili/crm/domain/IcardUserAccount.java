package com.dili.crm.domain;

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
 * This file was generated on 2017-11-30 15:46:52.
 */
@Table(name = "`icard_user_account`")
public interface IcardUserAccount extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="账号ID-具有生成规则")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`type`")
    @FieldDef(label="账号类型")
    @EditMode(editor = FieldEditor.Text, required = true)
    Byte getType();

    void setType(Byte type);

    @Column(name = "`fund_account_id`")
    @FieldDef(label="资金账号")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getFundAccountId();

    void setFundAccountId(Long fundAccountId);

    @Column(name = "`name`")
    @FieldDef(label="用户名", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getName();

    void setName(String name);

    @Column(name = "`gender`")
    @FieldDef(label="性别")
    @EditMode(editor = FieldEditor.Text, required = false)
    Byte getGender();

    void setGender(Byte gender);

    @Column(name = "`mobile`")
    @FieldDef(label="手机号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getMobile();

    void setMobile(String mobile);

    @Column(name = "`telphone`")
    @FieldDef(label="固定电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTelphone();

    void setTelphone(String telphone);

    @Column(name = "`id_code`")
    @FieldDef(label="身份证号码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getIdCode();

    void setIdCode(String idCode);

    @Column(name = "`address`")
    @FieldDef(label="联系地址", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddress();

    void setAddress(String address);

    @Column(name = "`login_pwd`")
    @FieldDef(label="登陆密码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getLoginPwd();

    void setLoginPwd(String loginPwd);

    @Column(name = "`trade_pwd`")
    @FieldDef(label="交易密码", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTradePwd();

    void setTradePwd(String tradePwd);

    @Column(name = "`force_pwd_change`")
    @FieldDef(label="是否强制修改登陆密码")
    @EditMode(editor = FieldEditor.Text, required = false)
    Byte getForcePwdChange();

    void setForcePwdChange(Byte forcePwdChange);

    @Column(name = "`last_login_time`")
    @FieldDef(label="最近登陆时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getLastLoginTime();

    void setLastLoginTime(Date lastLoginTime);

    @Column(name = "`secret_key`")
    @FieldDef(label="安全密钥", maxLength = 80)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSecretKey();

    void setSecretKey(String secretKey);

    @Column(name = "`organization_id`")
    @FieldDef(label="对公账户公司信息")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOrganizationId();

    void setOrganizationId(Long organizationId);

    @Column(name = "`institution_code`")
    @FieldDef(label="园区组织机构编码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getInstitutionCode();

    void setInstitutionCode(String institutionCode);

    @Column(name = "`institution_name`")
    @FieldDef(label="机构名称-保留字段", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getInstitutionName();

    void setInstitutionName(String institutionName);

    @Column(name = "`created_time`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    @Operator(Operator.GREAT_THAN)
    Date getCreatedTime();

    void setCreatedTime(Date createdTime);

    @Column(name = "`modified_time`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getModifiedTime();

    void setModifiedTime(Date modifiedTime);
}