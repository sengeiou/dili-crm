package com.dili.crm.domain.dto;

import com.dili.crm.domain.Customer;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import javax.persistence.Table;
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
}
