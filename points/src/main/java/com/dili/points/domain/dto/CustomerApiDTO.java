package com.dili.points.domain.dto;


import com.dili.points.domain.Customer;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import java.util.List;

/**
 * 客户接口的DTO，用于根据ids查询客户列表
 * @author wangmi
 */
public interface CustomerApiDTO extends Customer {

	@Operator(Operator.IN)
	@Column(name = "`id`")
	List<String> getIds();
	void setIds(List<String> ids);

}
