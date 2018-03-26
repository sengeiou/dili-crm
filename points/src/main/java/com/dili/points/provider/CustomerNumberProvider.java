package com.dili.points.provider;

import com.dili.points.domain.Customer;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户证件号远程批量提供者
 * Created by asiamaster on 2018/3/26.
 */
@Component
public class CustomerNumberProvider extends BatchDisplayTextProviderAdaptor {

	@Autowired
	private CustomerRpc customerRpc;

	@Override
	protected List getFkList(List<String> relationIds, Map metaMap) {
		CustomerApiDTO customerApiDTO = DTOUtils.newDTO(CustomerApiDTO.class);
		customerApiDTO.setCertificateNumbers(relationIds);
		BaseOutput<List<Customer>> output = customerRpc.list(customerApiDTO);
		return output.isSuccess() ? output.getData() : null;
	}

	@Override
	protected Map<String, String> getEscapeFileds(Map metaMap) {
		if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
			return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
		}else {
			Map<String, String> map = new HashMap<>();
			//默认显示客户名
			map.put(metaMap.get(FIELD_KEY).toString(), "name");
			return map;
		}
	}

	/**
	 * 关联(数据库)表的主键的字段名
	 * 默认取id，子类可自行实现
	 * @return
	 */
	@Override
	protected String getRelationTablePkField(Map metaMap) {
		return "certificateNumber";
	}

	@Override
	protected String getFkField(Map metaMap) {
		String field = (String)metaMap.get(FIELD_KEY);
		String fkField = (String)metaMap.get(FK_FILED_KEY);
		return fkField == null ? "certificateNumber" : fkField;
	}
}
