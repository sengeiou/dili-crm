package com.dili.sysadmin.sdk.component.beetl;

import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.redis.UserResourceRedis;
import com.dili.sysadmin.sdk.session.SessionContext;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 判断用户是否有资源访问权限，
 * 参数为资源编码resource.code
 * Created by asiamaster on 2017/7/26 0026.
 */
@Component("hasResource")
public class HasResourceFunction implements Function {

	@Autowired
	UserResourceRedis userResourceRedis;

	@Override
	public Object call(Object[] objects, Context context) {
		Object o = objects[0];
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		if(o == null || userTicket == null) {
			return false;
		}
		if(userResourceRedis.checkUserResourceRight(userTicket.getId(), String.valueOf(o))){
			return true;
		}
		return false;
	}
}
