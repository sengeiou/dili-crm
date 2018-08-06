package com.dili.points.domain.dto;


import com.dili.points.domain.Customer;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

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

	@Operator(Operator.IN)
	@Column(name = "`certificate_number`")
	List<String> getCertificateNumbers();
	void setCertificateNumbers(List<String> certificateNumbers);

	@Column(name = "`trading_firm_code`")
	@FieldDef(label="交易市场编码", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getTradingFirmCode();
	void setTradingFirmCode(String tradingFirmCode);

	@Operator(Operator.IN)
	@Column(name = "`trading_firm_code`")
	@FieldDef(label="交易市场编码", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = true)
	List<String> getTradingFirmCodes();
	void setTradingFirmCodes(List<String> tradingFirmCodes);

	/**
	 * 操作用户ID
	 * @return
	 */
	Long getUserId();
	void setUserId(Long userId);

}
