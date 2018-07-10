package com.dili.crm.rpc;

import com.dili.crm.domain.Department;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.retrofitful.annotation.POST;
import com.dili.ss.retrofitful.annotation.Restful;
import com.dili.ss.retrofitful.annotation.VOBody;
import com.dili.ss.retrofitful.annotation.VOSingleParameter;

import java.util.List;

/**
 * Created by asiamaster on 2017/10/19 0019.
 */
@Restful("${uap.contextPath}")
public interface DepartmentRpc {

	@POST("/departmentApi/get.api")
	BaseOutput<Department> get(@VOSingleParameter Long id);

	@POST("/departmentApi/listByExample.api")
	BaseOutput<List<Department>> listByExample(@VOBody Department department);
}
