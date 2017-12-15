package com.dili.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.crm.domain.DataDictionary;
import com.dili.crm.domain.DataDictionaryValue;
import com.dili.crm.service.ChartService;
import com.dili.crm.service.DataDictionaryService;
import com.dili.crm.service.DataDictionaryValueService;
import com.dili.ss.dto.DTOUtils;

@Service
public class ChartServiceImpl implements ChartService{
	@Autowired DataDictionaryService  dataDictionaryService;
	@Autowired DataDictionaryValueService dataDictionaryValueService;
	private DataDictionary findChartDictionary() {
		DataDictionary dataDictionary=this.dataDictionaryService.findByCode("ThirtPartChartUrls");
		return dataDictionary;
	}
	private String findUrl(String key) {
		DataDictionary dataDictionary=this.findChartDictionary();
		DataDictionaryValue condtion=DTOUtils.newDTO(DataDictionaryValue.class);
		condtion.setDdId(dataDictionary.getId());
		condtion.setValue(key);
		condtion.setYn(1);
		List<DataDictionaryValue>list=this.dataDictionaryValueService.list(condtion);
		if(list!=null&&list.size()==1) {
			return list.get(0).getCode();
		}
		return "";
	}
	public String getSalestopQuantityChartUrl() {
		String key="SalestopQuantityChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsalestop1Z0fttftttf0.html";
	}
	public String getSalestopAmountChartUrl() {
		String key="SalestopAmountChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsalestop2Z0fttftttf0.html";
	}
	public String getTradingClientChartUrl() {
		String key="TradingClientChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZtrading1Z0fttftttf0.html";
	}
	public String getTradingClientProductChartUrl() {
		String key="TradingClientProductChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZtrading2Z1fttftttf0.html";
	}
	public String getConsumptionQuantityChartUrl() {
		String key="ConsumptionQuantityChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZconsumption_top1Z0fttftttf0.html";
	}
	public String getConsumptionAmountChartUrl() {
		String key="ConsumptionAmountChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZconsumption_top2Z0fttftttf0.html";
	}
	public String getSalesareaChartUrl() {
		String key="SalesareaChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsalesareaZ0fttftttf0.html";
	}
	public String getSalesareaDetailsChartUrl() {
		String key="SalesareaDetailsChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsalesarea1Z0fttftttf0.html";
	}
	public String getSalesareaProductDetails() {
		String key="SalesareaProductDetails";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsalesarea2Z0fttftttf0.html";
	}
	
	public String getIndexAbnormalOrdersChartUrl() {
		String key="IndexAbnormalOrdersChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZabnormal_ordersZ0tffftfff0.html";
	}
	public String getIndexSalesTopChartUrl() {
		String key="IndexSalesTopChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsales_top_10Z5tfffffff0.html";
	}
	public String getIndexPurchasingTopChartUrl() {
		String key="IndexPurchasingTopChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZpurchasing_top_10Z5tfffffff0.html";
	}
	
	
	public String getClientSalesTopChartUrl() {
		String key="ClientSalesTopChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZesc_order_details1Z0tftftfff0.html";
	}
	public String getClientPurchasingTopChartUrl() {
		String key="ClientPurchasingTopChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZesc_order_details2Z0fftftfff0.html";
	}
	public String getClientUserContributionChartUrl() {
		String key="ClientUserContributionChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZuser_contributionZ0tftftfff0.html";
	}
	@Override
	public String getChartUrl(String key) {
		return this.findUrl(key);
	}
	
}
