package com.dili.points.domain.dto;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.dili.points.domain.CustomerPoints;
import com.dili.ss.domain.annotation.Operator;

public interface CustomerPointsDTO extends CustomerPoints{

    @Column(name = "`certificate_number`")
    @Operator(Operator.IN)
    List<String> getCertificateNumbers();
    void setCertificateNumbers(List<String> certificateNumbers);
	//客户名称
	public String getName();
	public void setName(String name) ;
	//组织类型
	public String getOrganizationType();
	public void setOrganizationType(String organizationType);
	//行业类型
	public String getProfession();
	public void setProfession(String profession);
	
	//客户类型
	public String getType();
	public void setType(String type) ;
	
	//证件类型
	public String getCertificateType();
	public void setCertificateType(String certificateType) ;
	//电话
	public String getPhone();
	public void setPhone(String phone) ;
	
	
	@Transient
	public boolean isBuyer();
	public void setBuyer(boolean isBuyer);

	@Transient
    Integer getActualPoints();
    void setActualPoints(Integer actualPoints);
}
