package com.dili.crm.domain.dto;

import com.dili.crm.domain.Customer;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2017-11-13 17:11:21.
 */
@Table(name = "`customer`")
public interface CustomerTreeDto extends Customer {

	/**
	 * 客户树的展开状态
	 * @return
	 */
	String getState();

	void setState(String state);

	@Column(name = "`market`")
	@Operator(Operator.IN)
	List<String> getFirmCodes();
	void setFirmCodes(List<String> firmCodes);

	/**
	 * 操作用户ID
	 * @return
	 */
	@Transient
	Long getUserId();
	void setUserId(Long userId);

}