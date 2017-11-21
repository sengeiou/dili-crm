package com.dili.crm.service.impl;

import com.dili.crm.cache.CrmCache;
import com.dili.crm.domain.City;
import com.dili.crm.domain.Department;
import com.dili.crm.rpc.DepartmentRpc;
import com.dili.crm.service.CacheService;
import com.dili.crm.service.CityService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 缓存服务
 */
@Service
public class CacheServiceImpl implements CacheService {

	@Autowired
	private DepartmentRpc departmentRpc;

	@Autowired
	private CityService cityService;

	@Override
	public void refreshDepartment() {
		//10秒以内不刷新部门缓存
		if (System.currentTimeMillis() - CrmCache.lastRefreshDepartmentMapTime <= 10000) {
			CrmCache.lastRefreshDepartmentMapTime = System.currentTimeMillis();
			return;
		}
		BaseOutput<List<Department>> output = departmentRpc.listByExample(null);
		if(output.getData() == null){
			return;
		}
		for (Department department : output.getData()) {
			CrmCache.departmentMap.put(department.getId(), department);
		}
		CrmCache.lastRefreshDepartmentMapTime = System.currentTimeMillis();
	}

	public void refreshCity() {
		//300秒以内不刷新城市缓存
		if (System.currentTimeMillis() - CrmCache.lastRefreshCityMapTime <= 300000) {
			CrmCache.lastRefreshCityMapTime = System.currentTimeMillis();
			return;
		}
		List<City> output = cityService.listByExample(DTOUtils.newDTO(City.class));
		for (City city : output) {
			CrmCache.cityMap.put(city.getId(), city);
		}
		CrmCache.lastRefreshCityMapTime = System.currentTimeMillis();
	}
}