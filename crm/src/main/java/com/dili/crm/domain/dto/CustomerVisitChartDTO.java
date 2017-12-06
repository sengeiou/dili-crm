package com.dili.crm.domain.dto;

import com.dili.ss.dto.IDTO;

/**
 * @author wangguofeng
 * 客户回访报表DTO(分别按类型mode,状态state 统计分组)
 */
public interface CustomerVisitChartDTO extends IDTO{
	//统计出的用户数量
	Integer getCount() ;
	void setCount(Integer count);
	
	//回访事件的类型
	String getMode();
	void setMode(String mode);
	
	//回访事件的状态
	String getState();
	void setState(String state) ;
	
	
}
