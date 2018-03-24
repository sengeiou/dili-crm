package com.dili.points.domain.dto;

import com.dili.points.domain.ExchangeCommodities;
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
 * @createTime 2018/3/22 14:04
 */
@Table(name = "`exchange_commodities`")
public interface ExchangeCommoditiesDTO extends ExchangeCommodities {

    @Column(name = "`modified`")
    @FieldDef(label="操作时间")
    @EditMode(editor = FieldEditor.Datetime)
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getModifiedStart();

    void setModifiedStart(Date modifiedStart);

    @Column(name = "`modified`")
    @FieldDef(label="操作时间")
    @EditMode(editor = FieldEditor.Datetime)
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getModifiedEnd();

    void setModifiedEnd(Date modifiedEnd);

}
