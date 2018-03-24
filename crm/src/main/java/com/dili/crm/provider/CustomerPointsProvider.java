package com.dili.crm.provider;

import com.dili.crm.domain.dto.CustomerPoints;
import com.dili.crm.domain.dto.CustomerPointsApiDTO;
import com.dili.crm.rpc.CustomerPointsRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户积分批量提供者
 * Created by asiamaster on 2018/3/24.
 */
@Component
public class CustomerPointsProvider extends BatchDisplayTextProviderAdaptor {

	@Autowired
	private CustomerPointsRpc customerPointsRpc;

	@Override
	protected List getFkList(List<String> relationIds, Map metaMap) {
		CustomerPointsApiDTO customerPointsApiDTO = DTOUtils.newDTO(CustomerPointsApiDTO.class);
		customerPointsApiDTO.setIds(relationIds);
		BaseOutput<List<CustomerPoints>> userOutput = customerPointsRpc.listCustomerPoints(customerPointsApiDTO);
		return userOutput.isSuccess() ? userOutput.getData() : null;
	}

	@Override
	protected Map<String, String> getEscapeFileds(Map metaMap) {
		if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
			return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
		}else {
			Map<String, String> map = new HashMap<>();
			map.put(metaMap.get(FIELD_KEY).toString(), "available");
			return map;
		}
	}

}
