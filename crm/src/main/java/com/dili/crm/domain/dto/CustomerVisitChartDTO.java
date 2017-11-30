package com.dili.crm.domain.dto;

import com.dili.ss.dto.IDTO;

/**
 * @author wangguofeng
 * 客户回访报表DTO(分别按类型mode,状态state 统计分组)
 */
public interface CustomerVisitChartDTO extends IDTO{
	
	Integer getCount() ;
	void setCount(Integer count);
	String getMode();
	void setMode(String mode);
	String getState();
	void setState(String state) ;
	
	
}
