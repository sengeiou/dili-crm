package com.dili.crm.service.impl;

import com.dili.crm.cache.CrmCache;
import com.dili.crm.domain.Department;
import com.dili.crm.rpc.DepartmentRpc;
import com.dili.crm.service.CacheService;
import com.dili.ss.domain.BaseOutput;
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

    @Override
    public void refreshDepartment(){
        //5秒以内不刷新部门缓存
        if(System.currentTimeMillis() - CrmCache.lastRefreshDepartmentMapTime <= 5000){
            CrmCache.lastRefreshDepartmentMapTime = System.currentTimeMillis();
            return;
        }
        BaseOutput<List<Department>> output = departmentRpc.listByExample(null);
        for(Department department : output.getData()){
            CrmCache.departmentMap.put(department.getId(), department);
        }
        CrmCache.lastRefreshDepartmentMapTime = System.currentTimeMillis();
    }
}