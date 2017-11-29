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
public interface MembersDto extends Customer {

	@Column(name = "`parent_id`")
	@Operator(Operator.NOT_EQUAL)
	Long getParentIdNotEqual();
	void setParentIdNotEqual(Long parentIdNotEqual);

	@Column(name = "`id`")
	@Operator(Operator.NOT_IN)
	List<String> getIdNotIn();
	void setIdNotIn(List<String> idNotIn);

}
