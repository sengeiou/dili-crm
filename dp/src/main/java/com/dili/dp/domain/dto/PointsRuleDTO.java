package com.dili.dp.domain.dto;

import com.dili.dp.domain.PointsRule;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2018-03-20 11:29:31.
 */
@Table(name = "`points_rule`")
public interface PointsRuleDTO extends PointsRule {

    @Column(name = "`name`")
    @FieldDef(label="规则名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCheckName();

    void setCheckName(String checkName);

    @Column(name = "`code`")
    @FieldDef(label="规则编码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCheckCode();

    void setCheckCode(String checkCode);


    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @Operator(Operator.GREAT_EQUAL_THAN)
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getStartCreated();

    void setStartCreated(Date startCreated);

    @Column(name = "`created`")
    @FieldDef(label="创建时间")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getEndCreated();

    void setEndCreated(Date endCreated);
    
    
	@Operator(Operator.IN)
	@Column(name = "`firm_code`")
	List<String> getFirmCodes();
	void setFirmCodes(List<String> firmCodes);

}
