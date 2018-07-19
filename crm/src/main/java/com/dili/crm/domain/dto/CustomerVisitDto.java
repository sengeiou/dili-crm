package com.dili.crm.domain.dto;

import com.dili.crm.domain.CustomerVisit;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.Column;
import java.util.List;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/7/19 14:41
 */
public interface CustomerVisitDto extends CustomerVisit {

    @Column(name = "`market`")
    @Operator(Operator.IN)
    @FieldDef(label="市场code集合")
    List<String> getFirmCodes();
    void setFirmCodes(List<String> firmCodes);

    @Column(name = "`market`")
    @FieldDef(label="单个市场信息")
    String getMarket();
    void setMarket(String market);
}
