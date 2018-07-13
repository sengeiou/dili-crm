package com.dili.points.provider;

import com.dili.points.domain.Customer;
import com.dili.points.domain.dto.CustomerApiDTO;
import com.dili.points.rpc.CustomerRpc;
import com.dili.points.rpc.FirmRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.redis.DataAuthRedis;
import com.dili.uap.sdk.session.SessionContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 市场提供者
 * Created by guofeng on 2018/07/12.
 */
@Component
public class FirmProvider implements ValueProvider {
 @Autowired DataAuthRedis dataAuthRedis;
 @Autowired FirmRpc firmRpc;
 	
 	/**
 	 * 当前用户拥有访问权限的firmcode
 	 * @return
 	 */
	public List<String> getCurrentUserFirmCodes() {
		List<Firm> list = this.getCurrentUserFirms();
		List<String> resultList = list.stream().map(Firm::getCode).collect(Collectors.toList());
		return resultList;
	}
	
	@Override
	public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
		
		List<Firm> list = this.getCurrentUserFirms();
		List<ValuePair<?>> resultList = list.stream().map(f->{
			return (ValuePair<?>)new ValuePairImpl(f.getName(), f.getCode());
		}).collect(Collectors.toCollection(()->new ArrayList<ValuePair<?>>()));
		resultList.add(0, new ValuePairImpl(EMPTY_ITEM_TEXT, null));
		return resultList;
	}

	@Override
	public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
		if (obj == null || "".equals(obj)) {
			return null;
		}
		String code = obj.toString();
		BaseOutput<Firm> out = firmRpc.getByCode(code);
		if (out.isSuccess()) {
			Firm firm = out.getData();
			return firm.getName();
		}
		return null;
	}
	
	/**
	 * 获得当前用户拥有的所有Firm
	 * @return
	 */
	private List<Firm> getCurrentUserFirms() {
		List<Firm> resultList = new ArrayList<>();
		List<Map> dataAuth = dataAuthRedis.dataAuth(DataAuthType.MARKET.getCode(),
				SessionContext.getSessionContext().getUserTicket().getId());
		if (dataAuth != null && !dataAuth.isEmpty()) {
			Map<Long, Firm> idFirmMap = this.getAllFirms().stream()
					.collect(Collectors.toMap(Firm::getId, Function.identity()));
			for (Map map : dataAuth) {
				Long id = Long.parseLong(String.valueOf(map.get("value")));
				if (idFirmMap.containsKey(id)) {
					resultList.add(idFirmMap.get(id));
				}

			}
		}
		return resultList;
	}
	
	/**
	 * 远程访问所有的Firm
	 * @return
	 */
	private List<Firm> getAllFirms() {
		BaseOutput<List<Firm>> out = firmRpc.listByExample(DTOUtils.newDTO(Firm.class));
		if (out.isSuccess()) {
			return out.getData();
		}
		return Collections.emptyList();
	}
}
