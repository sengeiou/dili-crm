package com.dili.dp.domain.dto;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.dili.dp.domain.CustomerPoints;
import com.dili.ss.domain.annotation.Operator;

public interface CustomerPointsDTO extends CustomerPoints{

    @Column(name = "`certificate_number`")
    @Operator(Operator.IN)
    List<String> getCertificateNumbers();
    void setCertificateNumbers(List<String> certificateNumbers);
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
    Integer getActualPoints();
    void setActualPoints(Integer actualPoints);
}
