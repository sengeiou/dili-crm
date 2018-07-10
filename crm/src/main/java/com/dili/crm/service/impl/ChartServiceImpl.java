package com.dili.crm.service.impl;


import com.dili.crm.rpc.DataDictionaryRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.crm.service.ChartService;
import com.dili.ss.dto.DTOUtils;

import java.util.List;

@Service
public class ChartServiceImpl implements ChartService{
	@Autowired
	private DataDictionaryRpc dataDictionaryRpc;

	private String findUrl(String key) {
		DataDictionaryValue condtion = DTOUtils.newDTO(DataDictionaryValue.class);
		condtion.setDdCode("ThirtPartChartUrls");
		condtion.setCode(key);
		BaseOutput<List<DataDictionaryValue>> output = this.dataDictionaryRpc.list(condtion);
		if(output.isSuccess()) {
			if (output.getData() != null && output.getData().size() == 1) {
				return output.getData().get(0).getCode();
			}
		}
		return "";
	}

	@Override
	public String getSalestopQuantityChartUrl() {
		String key="SalestopQuantityChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsalestop1Z0fttftttf0.html";
	}

	@Override
	public String getSalestopAmountChartUrl() {
		String key="SalestopAmountChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsalestop2Z0fttftttf0.html";
	}

	@Override
	public String getTradingClientChartUrl() {
		String key="TradingClientChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZtrading1Z0fttftttf0.html";
	}

	@Override
	public String getTradingClientProductChartUrl() {
		String key="TradingClientProductChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZtrading2Z1fttftttf0.html";
	}

	@Override
	public String getConsumptionQuantityChartUrl() {
		String key="ConsumptionQuantityChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZconsumption_top1Z0fttftttf0.html";
	}

	@Override
	public String getConsumptionAmountChartUrl() {
		String key="ConsumptionAmountChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZconsumption_top2Z0fttftttf0.html";
	}

	@Override
	public String getSalesareaChartUrl() {
		String key="SalesareaChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsalesareaZ0fttftttf0.html";
	}

	@Override
	public String getSalesareaDetailsChartUrl() {
		String key="SalesareaDetailsChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsalesarea1Z0fttftttf0.html";
	}

	@Override
	public String getSalesareaProductDetails() {
		String key="SalesareaProductDetails";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsalesarea2Z0fttftttf0.html";
	}

	@Override
	public String getIndexAbnormalOrdersChartUrl() {
		String key="IndexAbnormalOrdersChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZabnormal_ordersZ0tffftfff0.html";
	}

	@Override
	public String getIndexSalesTopChartUrl() {
		String key="IndexSalesTopChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZsales_top_10Z5tfffffff0.html";
	}

	@Override
	public String getIndexPurchasingTopChartUrl() {
		String key="IndexPurchasingTopChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZpurchasing_top_10Z5tfffffff0.html";
	}

	@Override
	public String getClientSalesTopChartUrl() {
		String key="ClientSalesTopChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZesc_order_details1Z0tftftfff0.html";
	}

	@Override
	public String getClientPurchasingTopChartUrl() {
		String key="ClientPurchasingTopChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZesc_order_details2Z0fftftfff0.html";
	}

	@Override
	public String getClientUserContributionChartUrl() {
		String key="ClientUserContributionChartUrl";
		return this.findUrl(key);//"http://ap.nong12.com/find/sZuser_contributionZ0tftftfff0.html";
	}

	@Override
	public String getChartUrl(String key) {
		return this.findUrl(key);
	}
	
}
