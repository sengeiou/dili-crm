package com.dili.points.domain.dto;

import com.dili.points.domain.PointsDetail;

public interface PointsDetailDTO extends PointsDetail {
	//客户ID
	public Long getCustomerId();
	public void setCustomerId(Long customerId);
}
