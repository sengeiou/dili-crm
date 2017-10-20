package com.dili.alm.provider;

import com.dili.alm.cache.AlmCache;
import com.dili.alm.domain.User;
import com.dili.alm.rpc.UserRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asiamaster on 2017/10/18 0018.
 */
@Component
public class MemberProvider implements ValueProvider {

	@Autowired
	private UserRpc userRpc;

	@PostConstruct
	public void init(){
		//应用启动时初始化userMap
		if(AlmCache.userMap.isEmpty()){
			BaseOutput<List<User>> output = userRpc.list(new User());
			if(output.isSuccess()){
				output.getData().forEach(user ->{
					AlmCache.userMap.put(user.getId(), user);
				});
			}
		}
	}
	@Override
	public List<ValuePair<?>> getLookupList(Object o, Map map, FieldMeta fieldMeta) {
		init();
		ArrayList buffer = new ArrayList<ValuePair<?>>();
		AlmCache.userMap.forEach((k, v)->{
			buffer.add(new ValuePairImpl(v.getRealName(), k));
		});
		return buffer;
	}

	@Override
	public String getDisplayText(Object o, Map map, FieldMeta fieldMeta) {
		if(o == null) return null;
		init();
		User user = AlmCache.userMap.get(o);
		return user == null ? null : user.getRealName();
	}
}
