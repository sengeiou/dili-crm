package com.dili.crm.domain.dto;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;

import com.dili.crm.domain.IcardUserCard;
import com.dili.ss.domain.annotation.Operator;
/**
 * 用于电子结算卡号信息查询
 * @author wangguofeng
 */
@Table(name = "`icard_user_card`")
public interface IcardUserCardDTO extends IcardUserCard{
	@Column(name = "`status`")
	@Operator(Operator.NOT_IN)
	List<Byte> getStatusNotIn();
	void setStatusNotIn(List<Byte> statusNotIn);
}
