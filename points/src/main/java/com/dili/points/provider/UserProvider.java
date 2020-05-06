package com.dili.points.provider;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.rpc.UserRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户批量提供者
 * Created by asiamaster on 2018/1/22.
 */
@Component
public class UserProvider extends BatchDisplayTextProviderAdaptor {

	@Autowired
	private UserRpc userRpc;

	@Override
	protected List getFkList(List<String> relationIds, Map metaMap) {
		BaseOutput<List<User>> userOutput = userRpc.listUserByIds(relationIds);
		return userOutput.isSuccess() ? userOutput.getData() : null;
	}

	@Override
	protected Map<String, String> getEscapeFileds(Map metaMap) {
		if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
			return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
		}else {
			Map<String, String> map = new HashMap<>();
			map.put(metaMap.get(FIELD_KEY).toString(), "realName");
			return map;
		}
	}

}
