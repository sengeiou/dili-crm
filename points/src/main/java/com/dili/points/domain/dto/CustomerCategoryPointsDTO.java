package com.dili.points.domain.dto;

import java.math.BigDecimal;

import com.dili.points.domain.CustomerCategoryPoints;
import com.dili.points.domain.OrderItem;

public interface CustomerCategoryPointsDTO extends CustomerCategoryPoints,OrderItem{
	BigDecimal getPercentage();
    void setPercentage(BigDecimal percentage);
}
