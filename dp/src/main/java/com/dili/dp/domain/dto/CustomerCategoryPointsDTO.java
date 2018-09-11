package com.dili.dp.domain.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.dili.dp.domain.CustomerCategoryPoints;
import com.dili.dp.domain.OrderItem;
import com.dili.ss.domain.annotation.Operator;

public interface CustomerCategoryPointsDTO extends CustomerCategoryPoints,OrderItem{
	BigDecimal getPercentage();
    void setPercentage(BigDecimal percentage);
    
	@Operator(Operator.IN)
	@Column(name = "`firm_code`")
	List<String> getFirmCodes();
	void setFirmCodes(List<String> firmCodes);
	
	
	@Transient
	public boolean isBuyer();
	public void setBuyer(boolean isBuyer);

    @Transient
    public Integer getActualPoints();
	public void setActualPoints(Integer actualPoints) ;
	
}
