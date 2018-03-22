package com.dili.points.domain.dto;

import com.dili.points.domain.PointsExchangeRecord;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * <B>Description</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/3/22 15:44
 */
@Table(name = "`points_exchange_record`")
public interface PointsExchangeRecordDTO extends PointsExchangeRecord {

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
