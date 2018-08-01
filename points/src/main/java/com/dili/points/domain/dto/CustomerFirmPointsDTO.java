package com.dili.points.domain.dto;

import javax.persistence.Transient;

import com.dili.points.domain.CustomerFirmPoints;

public interface CustomerFirmPointsDTO extends CustomerFirmPoints{
	
	@Transient
	public boolean isBuyer();
	public void setBuyer(boolean isBuyer);

	@Transient
    Integer getPoints();
    void setPoints(Integer points);

	@Transient
    Integer getActualPoints();
    void setActualPoints(Integer actualPoints);
}
