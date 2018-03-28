package com.dili.points.domain.dto;

import com.dili.points.domain.PointsDetail;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "`points_detail`")
public interface PointsDetailDTO extends PointsDetail {
	//客户ID
	@Transient
	Long getCustomerId();
	void setCustomerId(Long customerId);
	
    //是否需要恢复(0否,1是)
	@Transient
	Integer getNeedRecover();
    void setNeedRecover(Integer needRecover);
    

	@Column(name = "`created`")
	@FieldDef(label="查询条件-开始时间")
	@EditMode(editor = FieldEditor.Datetime)
	@Operator(Operator.GREAT_EQUAL_THAN)
	Date getCreatedStart();

	void setCreatedStart(Date createdStart);

	@Column(name = "`created`")
	@FieldDef(label="查询条件-结束时间")
	@EditMode(editor = FieldEditor.Datetime)
	@Operator(Operator.LITTLE_EQUAL_THAN)
	Date getCreatedEnd();

	void setCreatedEnd(Date createdEnd);

}
