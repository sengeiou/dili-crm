package com.dili.sysadmin.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.sysadmin.domain.User;
import com.dili.sysadmin.manager.SessionRedisManager;
import com.dili.sysadmin.sdk.session.SessionConstants;
import com.dili.sysadmin.sdk.util.ManageRedisUtil;

/**
 * Created by root on 5/20/15.
 */
@Component
public class SessionRedisManagerImpl implements SessionRedisManager {

	@Autowired
	private ManageRedisUtil myRedisUtil;

	@Override
    public void setUserIdSessionDataKey(User user, String session) {
		JSONObject map = new JSONObject();
		map.put("sessionId", session);
		map.put("user", JSON.toJSONString(user));
		myRedisUtil.set(SessionConstants.USERID_SESSION_KEY + user.getId(), map.toString(),
				SessionConstants.SESSIONID_USERID_TIMEOUT);
	}

	public String getUserIdSessionDataKey(String userId) {
		return myRedisUtil.get(SessionConstants.USERID_SESSION_KEY + userId, String.class);
	}

	@Override
	public Boolean existUserIdSessionDataKey(String s) {
		return myRedisUtil.exists(SessionConstants.USERID_SESSION_KEY + s);
	}

	@Override
	public void addKickSessionKey(String oldSessionId) {
		myRedisUtil.set(SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId, "",
				SessionConstants.COOKIE_TIMEOUT.longValue());
	}

	@Override
	public Boolean existKickSessionKey(String oldSessionId) {
		return myRedisUtil.exists(SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId);
	}

	@Override
	public void clearKickSessionKey(String oldSessionId) {
		myRedisUtil.remove(SessionConstants.KICK_OLDSESSIONID_KEY + oldSessionId);
	}

	// sessionId - userId 操作 - START
	@Override
    public void setSessionUserIdKey(String sessionId, String userId) {
		myRedisUtil.set(SessionConstants.SESSION_USERID_KEY + sessionId, userId,
				SessionConstants.SESSIONID_USERID_TIMEOUT);
	}

	@Override
    public String getSessionUserIdKey(String sessionId) {
		String rst = null;
		rst = myRedisUtil.get(SessionConstants.SESSION_USERID_KEY + sessionId, String.class);
		return rst;
	}

	@Override
    public void clearSessionUserIdKey(String sessionId) {
		myRedisUtil.remove(SessionConstants.SESSION_USERID_KEY + sessionId);
	}

	@Override
	public void clearUserIdSessionDataKey(String userId) {
		myRedisUtil.remove(SessionConstants.USERID_SESSION_KEY + userId);
	}
	// sessionId - userId 操作 - END
}
