package com.dili.crm.domain.dto;

import com.dili.crm.domain.Customer;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 客户接口的DTO，用于根据ids查询客户列表
 * @author wangmi
 */
@Table(name = "`customer`")
public interface CustomerApiDTO extends Customer {

	@Operator(Operator.IN)
	@Column(name = "`id`")
	List<Long> getIds();
	void setIds(List<Long> ids);

	@Operator(Operator.IN)
	@Column(name = "`certificate_number`")
	List<String> getCertificateNumbers();
	void setCertificateNumbers(List<String> certificateNumbers);

	/**
	 * 操作用户ID
	 * @return
	 */
	@Transient
	Long getUserId();
	void setUserId(Long userId);

}
