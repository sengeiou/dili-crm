package com.dili.crm.service.impl;


import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.crm.rpc.DataDictionaryRpc;
import com.dili.crm.service.ChartService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;

@Service
public class ChartServiceImpl implements ChartService{
	@Autowired
	private DataDictionaryRpc dataDictionaryRpc;


	@Override
	public String getSalestopQuantityChartUrl(String firmCode) {
		String key="SalestopQuantityChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZsalestop1Z0fttftttf0.html";
	}

	@Override
	public String getSalestopAmountChartUrl(String firmCode) {
		String key="SalestopAmountChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZsalestop2Z0fttftttf0.html";
	}

	@Override
	public String getTradingClientChartUrl(String firmCode) {
		String key="TradingClientChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZtrading1Z0fttftttf0.html";
	}

	@Override
	public String getTradingClientProductChartUrl(String firmCode) {
		String key="TradingClientProductChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZtrading2Z1fttftttf0.html";
	}

	@Override
	public String getConsumptionQuantityChartUrl(String firmCode) {
		String key="ConsumptionQuantityChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZconsumption_top1Z0fttftttf0.html";
	}

	@Override
	public String getConsumptionAmountChartUrl(String firmCode) {
		String key="ConsumptionAmountChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZconsumption_top2Z0fttftttf0.html";
	}

	@Override
	public String getSalesareaChartUrl(String firmCode) {
		String key="SalesareaChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZsalesareaZ0fttftttf0.html";
	}

	@Override
	public String getSalesareaDetailsChartUrl(String firmCode) {
		String key="SalesareaDetailsChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZsalesarea1Z0fttftttf0.html";
	}

	@Override
	public String getSalesareaProductDetails(String firmCode) {
		String key="SalesareaProductDetails";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZsalesarea2Z0fttftttf0.html";
	}

	@Override
	public String getIndexAbnormalOrdersChartUrl(String firmCode) {
		String key="IndexAbnormalOrdersChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZabnormal_ordersZ0tffftfff0.html";
	}

	@Override
	public String getIndexSalesTopChartUrl(String firmCode) {
		String key="IndexSalesTopChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZsales_top_10Z5tfffffff0.html";
	}

	@Override
	public String getIndexPurchasingTopChartUrl(String firmCode) {
		String key="IndexPurchasingTopChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZpurchasing_top_10Z5tfffffff0.html";
	}

	@Override
	public String getClientSalesTopChartUrl(String firmCode) {
		String key="ClientSalesTopChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZesc_order_details1Z0tftftfff0.html";
	}

	@Override
	public String getClientPurchasingTopChartUrl(String firmCode) {
		String key="ClientPurchasingTopChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZesc_order_details2Z0fftftfff0.html";
	}

	@Override
	public String getClientUserContributionChartUrl(String firmCode) {
		String key="ClientUserContributionChartUrl";
		return this.findUrl(key,firmCode);//"http://ap.nong12.com/find/sZuser_contributionZ0tftftfff0.html";
	}

	@Override
	public String getChartUrl(String key,String firmCode) {
		return this.findUrl(key,firmCode);
	}
	

	
	/**
	 * 通过key查找url并进行格式化
	 * @param key
	 * @param firmCode
	 * @return
	 */
	private String findUrl(String key,String firmCode) {
		DataDictionaryValue condtion = DTOUtils.newDTO(DataDictionaryValue.class);
		condtion.setDdCode("ThirtPartChartUrls");
		condtion.setName(key);
		BaseOutput<List<DataDictionaryValue>> output = this.dataDictionaryRpc.list(condtion);
		String url = "";
		if(output.isSuccess()) {
			if (output.getData() != null && output.getData().size() == 1) {
				url= output.getData().get(0).getCode();
			}
		}
		return this.formatUrl(url, firmCode);
	}
	
	/**
	 * 对url进行格式化，替换其中的占位符
	 * @param url
	 * @param firmCode
	 * @return
	 */
	private String formatUrl(String url,String firmCode) {
		
		/*48 哈尔滨
		49	沈阳
		50	贵阳
		51	牡丹江
		52	齐齐哈尔
		53	长春
		59	寿光
		 */
		
		DataDictionaryValue condtion = DTOUtils.newDTO(DataDictionaryValue.class);
		condtion.setDdCode("firmcode_reportproxy");
		condtion.setName(firmCode);
		BaseOutput<List<DataDictionaryValue>> output = this.dataDictionaryRpc.list(condtion);
		String proxyValue = "";
		if(output.isSuccess()) {
			if (output.getData() != null && output.getData().size() == 1) {
				proxyValue= output.getData().get(0).getCode();
			}
		}
		url= MessageFormat.format(url, new Object[]{proxyValue});
		return url;
	}
	
}
