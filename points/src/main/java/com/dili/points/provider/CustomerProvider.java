package com.dili.points.provider;

import com.dili.points.domain.Customer;
import com.dili.points.domain.User;
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
 * 客户远程批量提供者
 * Created by asiamaster on 2018/3/22.
 */
@Component
public class CustomerProvider extends BatchDisplayTextProviderAdaptor {

	@Autowired
	private CustomerRpc customerRpc;

	@Override
	protected List getFkList(List<String> relationIds, Map metaMap) {
		CustomerApiDTO customerApiDTO = DTOUtils.newDTO(CustomerApiDTO.class);
		customerApiDTO.setIds(relationIds);
		BaseOutput<List<Customer>> output = customerRpc.list(customerApiDTO);
		return output.isSuccess() ? output.getData() : null;
	}

	@Override
	protected Map<String, String> getEscapeFileds(Map metaMap) {
		if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
			return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
		}else {
			Map<String, String> map = new HashMap<>();
			map.put(metaMap.get(FIELD_KEY).toString(), "name");
			return map;
		}
	}

}
