package com.dili.crm.service.impl;


import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.crm.provider.FirmProvider;
import com.dili.crm.rpc.DataDictionaryRpc;
import com.dili.crm.service.ChartService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;

@Service
public class ChartServiceImpl implements ChartService{
	@Autowired
	private DataDictionaryRpc dataDictionaryRpc;
	@Autowired FirmProvider firmProvider;


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
