package com.dili.crm.domain.dto;

import com.dili.ss.dto.IDTO;

/**
 * @author wangguofeng
 * 客户报表DTO(分别按行业profession,市场market,类型type 统计分组)
 */
public interface CustomerChartDTO  extends IDTO{

	//统计出的用户数量
	Integer getCount() ;
	void setCount(Integer count);
	
	//行业名称
	String getProfession() ;
	void setProfession(String profession) ;

	//市场名称
	String getMarket();
	void setMarket(String market);

	//用户类型
	String getType();
	void setType(String type);
	
}
