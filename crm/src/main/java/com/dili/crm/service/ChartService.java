package com.dili.crm.service;

public interface ChartService {

	String getIndexAbnormalOrdersChartUrl(String firmCode);

	String getIndexSalesTopChartUrl(String firmCode);

	String getIndexPurchasingTopChartUrl(String firmCode);

	String getClientSalesTopChartUrl(String firmCode);

	String getClientPurchasingTopChartUrl(String firmCode);

	String getClientUserContributionChartUrl(String firmCode);

	String getChartUrl(String key,String firmCode);
}
