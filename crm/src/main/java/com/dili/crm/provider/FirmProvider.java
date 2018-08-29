package com.dili.crm.provider;

import com.alibaba.fastjson.JSONPath;
import com.dili.crm.domain.dto.FirmDto;
import com.dili.crm.service.FirmService;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.sdk.domain.Firm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 市场提供者
 * Created by guofeng on 2018/07/12.
 */
@Component
public class FirmProvider extends BatchDisplayTextProviderAdaptor {

	@Autowired
	private FirmService firmService;

	@Override
	public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
		//是否要请选择
		Object withEmptyOptValue=JSONPath.read(String.valueOf(metaMap.get("queryParams")), "/withEmptyOpt");
		//是否要显示集团
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
	protected List getFkList(List<String> relationIds, Map metaMap) {
		if(relationIds != null) {
			List<String> firmCodes = relationIds.stream()
					.filter(Objects::nonNull)
					.distinct()
					.map(String::toLowerCase)
					.collect(Collectors.toList());
			if(!firmCodes.isEmpty()) {
				FirmDto firmDto = DTOUtils.newDTO(FirmDto.class);
				firmDto.setCodes(firmCodes);
				List<Firm> firms = firmService.listByExample(firmDto);
				return firms;
			}
		}
		return null;
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

	@Override
	protected boolean ignoreCaseToRef() {
		return true;
	}

	/**
	 * 关联(数据库)表的主键的字段名
	 * 默认取id，子类可自行实现
	 * @return
	 */
	@Override
	protected String getRelationTablePkField(Map metaMap) {
		return "code";
	}
}
