package com.dili.crm.service;

public interface ChartService {
	String getSalestopQuantityChartUrl(String firmCode);

	String getSalestopAmountChartUrl(String firmCode);

	String getTradingClientChartUrl(String firmCode);

	String getTradingClientProductChartUrl(String firmCode);

	String getConsumptionQuantityChartUrl(String firmCode);

	String getConsumptionAmountChartUrl(String firmCode);

	String getSalesareaChartUrl(String firmCode);

	String getSalesareaDetailsChartUrl(String firmCode);

	String getSalesareaProductDetails(String firmCode);

	String getIndexAbnormalOrdersChartUrl(String firmCode);

	String getIndexSalesTopChartUrl(String firmCode);

	String getIndexPurchasingTopChartUrl(String firmCode);

	String getClientSalesTopChartUrl(String firmCode);

	String getClientPurchasingTopChartUrl(String firmCode);

	String getClientUserContributionChartUrl(String firmCode);

	String getChartUrl(String key,String firmCode);
}
