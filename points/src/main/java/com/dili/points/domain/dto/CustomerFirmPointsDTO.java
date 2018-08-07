package com.dili.points.domain.dto;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.dili.points.domain.CustomerFirmPoints;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import java.util.List;

public interface CustomerFirmPointsDTO extends CustomerFirmPoints{

	@Column(name = "`certificate_number`")
	@Operator(Operator.IN)
	List<String> getCertificateNumbers();
	void setCertificateNumbers(List<String> certificateNumbers);

	@Operator(Operator.IN)
	@Column(name = "`trading_firm_code`")
	List<String> getTradingFirmCodes();
	void setTradingFirmCodes(List<String> tradingFirmCodes);

	//客户名称
	String getName();
	void setName(String name) ;
	//组织类型
	String getOrganizationType();
	void setOrganizationType(String organizationType);
	//行业类型
	String getProfession();
	void setProfession(String profession);

	//客户类型
	String getType();
	void setType(String type) ;

	//证件类型
	String getCertificateType();
	void setCertificateType(String certificateType) ;

	//电话
	String getPhone();
	void setPhone(String phone) ;

	@Transient
	boolean isBuyer();
	void setBuyer(boolean isBuyer);

	@Transient
    Integer getPoints();
    void setPoints(Integer points);

    //调整时的实际积分(可能会由于每日积分上限限制)，用于调整积分,保存CustomerFirmPointsDTO之后将实际保存的积分通过actualPoints传递回来
	@Transient
    Integer getActualPoints();
    void setActualPoints(Integer actualPoints);
}
