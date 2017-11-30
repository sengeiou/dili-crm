package com.dili.crm.domain.dto;

import com.dili.ss.dto.IDTO;

/**
 * @author wangguofeng
 * 客户报表DTO(分别按行业profession,市场market,类型type 统计分组)
 */
public interface CustomerChartDTO  extends IDTO{

	Integer getCount() ;
	void setCount(Integer count);
	String getProfession() ;
	void setProfession(String profession) ;
	String getMarket();
	void setMarket(String market);
	String getType();
	void setType(String type);
	
}
