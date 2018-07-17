package com.dili.crm.domain.dto;

import com.dili.crm.domain.Customer;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * 用于成员客户查询
 * Created by asiamaster on 2017/11/29 0029.
 */
@Table(name = "`customer`")
public interface CustomerDto extends Customer {

	@Column(name = "`market`")
	@Operator(Operator.IN)
	List<String> getFirmCodes();
	void setFirmCodes(List<String> firmCodes);

	@Column(name = "`created`")
	@FieldDef(label="创建时间")
	@EditMode(editor = FieldEditor.Datetime)
	@Operator(Operator.LITTLE_EQUAL_THAN)
	Date getCreatedEnd();
	void setCreatedEnd(Date created);

	@Column(name = "`name`")
	@FieldDef(label="客户名称,用户直接等于操作时")
	String getEqualName();
	void setEqualName(String name);
}
