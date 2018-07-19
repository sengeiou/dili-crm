package com.dili.crm.provider;

import com.dili.crm.rpc.UserRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用户提供者
 * Created by asiamaster on 2017/10/18 0018.
 */
@Component
public class UserProvider implements ValueProvider {

	@Autowired
	private UserRpc userRpc;

	@Override
	public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
		return null;
	}

	@Override
	public String getDisplayText(Object o, Map map, FieldMeta fieldMeta) {
		if(o == null) {
			return null;
		}
		BaseOutput<User> userBaseOutput = userRpc.get(Long.parseLong(o.toString()));
		return userBaseOutput.isSuccess() ? userBaseOutput.getData().getRealName() : null;
	}
}
