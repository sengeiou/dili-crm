package com.dili.points.domain.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dili.points.domain.Customer;
import com.dili.points.domain.OrderItem;
import com.dili.points.domain.PointsRule;

/**
 * 用于在计算积分过程中传递数据
 * @author wangguofeng
 *
 */
public class OrderPointsDataInfo {
	
	private String certificateNumber;
	private Integer inOut;
	private Date created;
	private String customerType;
	private BigDecimal points;
	private Integer actualPoints;
	private String sourceSystem;
	private String firmCode;
	private String notes;
	private Integer generateWay;
	private Integer weightType;
	
	
	private String orderCode;
	private String orderType;
	
	private Long totalMoney;
	private BigDecimal weight;
	private Integer payment;
	
	
	private Customer customer;
	private List<OrderItem>orderItems;
	private  PointsRule pointsRule;
	
	private boolean isBuyer;
	
	
	public Integer getPayment() {
		return payment;
	}
	public void setPayment(Integer payment) {
		this.payment = payment;
	}
	public Long getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Long totalMoney) {
		this.totalMoney = totalMoney;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public PointsRule getPointsRule() {
		return pointsRule;
	}
	public void setPointsRule(PointsRule pointsRule) {
		this.pointsRule = pointsRule;
	}
	public Integer getActualPoints() {
		return actualPoints;
	}
	public void setActualPoints(Integer actualPoints) {
		this.actualPoints = actualPoints;
	}
	public boolean isBuyer() {
		return isBuyer;
	}
	public void setBuyer(boolean isBuyer) {
		this.isBuyer = isBuyer;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public Integer getWeightType() {
		return weightType;
	}
	public void setWeightType(Integer weightType) {
		this.weightType = weightType;
	}
	public Integer getGenerateWay() {
		return generateWay;
	}
	public void setGenerateWay(Integer generateWay) {
		this.generateWay = generateWay;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getFirmCode() {
		return firmCode;
	}
	public void setFirmCode(String firmCode) {
		this.firmCode = firmCode;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	public Integer getInOut() {
		return inOut;
	}
	public void setInOut(Integer inOut) {
		this.inOut = inOut;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public BigDecimal getPoints() {
		return points;
	}
	public void setPoints(BigDecimal points) {
		this.points = points;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	@Override
	public String toString() {
		return "OrderPointsDataInfo [certificateNumber=" + certificateNumber + ", inOut=" + inOut + ", created="
				+ created + ", customerType=" + customerType + ", points=" + points + ", actualPoints=" + actualPoints
				+ ", sourceSystem=" + sourceSystem + ", firmCode=" + firmCode + ", notes=" + notes + ", generateWay="
				+ generateWay + ", weightType=" + weightType + ", orderCode=" + orderCode + ", orderType=" + orderType
				+ ", totalMoney=" + totalMoney + ", weight=" + weight + ", payment=" + payment + ", customer="
				+ customer + ", orderItems=" + orderItems + ", pointsRule=" + pointsRule + ", isBuyer=" + isBuyer + "]";
	}
	
	
	
}
