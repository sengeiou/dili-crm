package com.dili.crm.service;

public interface ChartService {
	String getSalestopQuantityChartUrl();

	String getSalestopAmountChartUrl();

	String getTradingClientChartUrl();

	String getTradingClientProductChartUrl();

	String getConsumptionQuantityChartUrl();

	String getConsumptionAmountChartUrl();

	String getSalesareaChartUrl();

	String getSalesareaDetailsChartUrl();

	String getSalesareaProductDetails();

	String getIndexAbnormalOrdersChartUrl();

	String getIndexSalesTopChartUrl();

	String getIndexPurchasingTopChartUrl();

	String getClientSalesTopChartUrl();

	String getClientPurchasingTopChartUrl();

	String getClientUserContributionChartUrl();

	String getChartUrl(String key);
}
