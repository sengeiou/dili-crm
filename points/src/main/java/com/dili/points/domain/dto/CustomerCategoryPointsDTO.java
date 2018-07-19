package com.dili.points.domain.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;

import com.dili.points.domain.CustomerCategoryPoints;
import com.dili.points.domain.OrderItem;
import com.dili.ss.domain.annotation.Operator;

public interface CustomerCategoryPointsDTO extends CustomerCategoryPoints,OrderItem{
	BigDecimal getPercentage();
    void setPercentage(BigDecimal percentage);
    
	@Operator(Operator.IN)
	@Column(name = "`firm_code`")
	List<String> getFirmCodes();
	void setFirmCodes(List<String> firmCodes);
	
}
