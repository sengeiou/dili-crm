package com.dili.crm.cache;

import com.dili.crm.domain.City;
import com.dili.crm.domain.Department;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by asiamaster on 2017/10/19 0019.
 */
public class CrmCache {
	//上次刷新部门的时间(控制在某个时间段内不刷新)
	public static long lastRefreshDepartmentMapTime = 0L;
	// 缓存部门(key为部门id，value为Department)
	public static final Map<Long, Department> DEPARTMENT_Map = new ConcurrentHashMap<>();

	//上次刷新城市的时间(控制在某个时间段内不刷新)
	public static long lastRefreshCityMapTime = 0L;
	// 缓存城市(key为城市id，value为City)
	public static final Map<Long, City> CITY_MAP = new ConcurrentHashMap<>();


}
