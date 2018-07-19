package com.dili.crm.provider;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.dili.crm.rpc.DataAuthRpc;
import com.dili.crm.rpc.FirmRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserDataAuth;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 市场提供者
 * Created by guofeng on 2018/07/12.
 */
@Component
public class FirmProvider implements ValueProvider {
 @Autowired DataAuthRedis dataAuthRedis;
 @Autowired DataAuthRpc dataAuthRpc;
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
	
	/**
 	 * 当前用户拥有访问权限的firmcode
 	 * @return
 	 */
	public List<String> getCurrentUserAvaliableFirmCodes(String firmCode) {
		List<Firm> list = this.getCurrentUserFirms();
		List<String> resultList = list.stream().map(Firm::getCode).collect(Collectors.toList());
		if(StringUtils.isBlank(firmCode)||!resultList.contains(firmCode)) {
			return resultList;
		}else {
			return Arrays.asList(firmCode);
		}
	}
	/**
 	 * 当前用户拥有访问权限的firmcode
 	 * @return
 	 */
	public String getCurrentUserDefaultFirmCode() {
		String defaultFirmCode="hd";
		List<Firm> list = this.getCurrentUserFirms();
		List<String> resultList = list.stream().map(Firm::getCode).collect(Collectors.toList());
		if(resultList.contains(defaultFirmCode)) {
			return defaultFirmCode;
		}else if(resultList.size()>0){
			return resultList.get(0);
		}else {
			return "";
		}
	}
	@Override
	public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
		
		Object withEmptyOptValue=JSONPath.read(String.valueOf(metaMap.get("queryParams")), "/withEmptyOpt");	
		
		Object withGroupOptValue=JSONPath.read(String.valueOf(metaMap.get("queryParams")), "/withGroupOpt");	
		
		
		List<Firm> list = this.getCurrentUserFirms();
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
		Optional<Firm> opt = this.getFirmByCode(code);
		return opt.map(Firm::getName).orElse(null);
	}
	/**
	 * 通过code查询firm
	 * @return
	 */
	public Optional<Firm> getFirmByCode(String firmCode) {
		if (StringUtils.isBlank(firmCode)) {
			return Optional.empty();
		}
		BaseOutput<Firm> out = firmRpc.getByCode(firmCode);
		if (out.isSuccess()) {
			Firm firm = out.getData();
			return Optional.ofNullable(firm);
		}
		return Optional.empty();
	}
	/**
	 * 获得当前用户拥有的所有Firm
	 * @return
	 */
	private List<Firm> getCurrentUserFirms() {
		
		UserDataAuth userDataAuth=DTOUtils.newDTO(UserDataAuth.class);
		userDataAuth.setUserId(SessionContext.getSessionContext().getUserTicket().getId());
		userDataAuth.setRefCode(DataAuthType.MARKET.getCode());
		
		BaseOutput<List<Map>>out=dataAuthRpc.listUserDataAuthDetail(userDataAuth);
		if(out.isSuccess()) {
			Stream<Firm>stream=	out.getData().stream().flatMap(m->m.values().stream())
			.map(json->{
				JSONObject obj=(JSONObject)json;
				Firm firm=DTOUtils.newDTO(Firm.class);
				firm.setCode(String.valueOf(obj.get("code")));
				firm.setName(String.valueOf(obj.get("name")));
				return firm;
			}
			);
			return stream.collect(Collectors.toList());
		}else {
			return Collections.emptyList();
		}
	}
	
public static void main(String[] args) {
	Object obj=JSONPath.read(null, "/abc");
	System.out.println(obj);
}
}
