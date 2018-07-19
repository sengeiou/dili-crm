package com.dili.crm.provider;

import com.alibaba.fastjson.JSONPath;
import com.dili.crm.service.FirmService;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.Firm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 市场提供者
 * Created by guofeng on 2018/07/12.
 */
@Component
public class FirmProvider implements ValueProvider {

	@Autowired
	private FirmService firmService;

	@Override
	public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
		Object withEmptyOptValue=JSONPath.read(String.valueOf(metaMap.get("queryParams")), "/withEmptyOpt");
		Object withGroupOptValue=JSONPath.read(String.valueOf(metaMap.get("queryParams")), "/withGroupOpt");
		List<Firm> list = firmService.getCurrentUserFirms();
		List<ValuePair<?>> resultList = list.stream().filter((f)->{
			if(Boolean.FALSE.equals(withGroupOptValue)&&f.getCode().equalsIgnoreCase("group")) {
				return false;
			}
			return true;

		}).map(f->{
			return (ValuePair<?>)new ValuePairImpl(f.getName(), f.getCode());
		}).collect(Collectors.toCollection(()->new ArrayList<ValuePair<?>>()));
		if(!Boolean.FALSE.equals(withEmptyOptValue)) {
			resultList.add(0, new ValuePairImpl(EMPTY_ITEM_TEXT, null));
		}
		return resultList;
	}

	@Override
	public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
		if (obj == null || "".equals(obj)) {
			return null;
		}
		String code = obj.toString();
		Optional<Firm> opt = firmService.getFirmByCode(code);
		return opt.map(Firm::getName).orElse(null);
	}
}
