package com.dili.sysadmin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.sysadmin.domain.dto.DataAuthConfDto;
import com.dili.sysadmin.domain.dto.DataDictionaryDto;
import com.dili.sysadmin.domain.dto.DataDictionaryValueDto;
import com.dili.sysadmin.rpc.DataDictrionaryRPC;
import com.dili.sysadmin.service.DataItemAuthService;

/**
 * Created by Administrator on 2016/10/17.
 */
@Service("DataItemAuthServiceImpl")
public class DataItemAuthServiceImpl implements DataItemAuthService {

	private static final Logger log = LoggerFactory.getLogger(DataItemAuthServiceImpl.class);

	public static final String DATA_AUTH_CODE = "data_auth_type";
	public static final String DATA_AUTH_TYPE = "dataAuth";

	@Autowired
	private DataDictrionaryRPC dataDictionaryRPC;

	@Override
	public String fetchType() {
		return DATA_AUTH_TYPE;
	}

	public List<DataAuthConfDto> fetchItemList() {
		BaseOutput<DataDictionaryDto> output = this.dataDictionaryRPC.findDataDictionaryByCode(DATA_AUTH_CODE);
		if (!output.getCode().equals(ResultCode.OK)) {
			log.error(output.getResult());
			return null;
		}
		DataDictionaryDto dataDictionary = output.getData();
		List<DataDictionaryValueDto> values = dataDictionary.getValues();
		if (CollectionUtils.isEmpty(values)) {
			return null;
		}
		List<DataAuthConfDto> list = new ArrayList<>(values.size());
		for (DataDictionaryValueDto value : values) {
			DataAuthConfDto conf = new DataAuthConfDto();
			conf.setLabel(value.getValue());
			conf.setType(value.getCode());
			String json = value.getRemark();
			Map<String, Boolean> map = JSON.parseObject(json, new TypeReference<Map<String, Boolean>>() {
			});
			conf.setChange(map.get("change"));
			conf.setShow(map.get("show"));
			list.add(conf);
		}
		return list;
	}

}
