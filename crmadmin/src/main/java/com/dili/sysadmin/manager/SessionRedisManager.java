package com.dili.sysadmin.manager;

import com.dili.sysadmin.domain.User;

/**
 * Created by root on 5/20/15.
 */
public interface SessionRedisManager {

    public void clearSessionUserIdKey(String sessionId);

    public void clearUserIdSessionDataKey(String userId);

    public void setSessionUserIdKey(String sessionId, String userId);

    public String getSessionUserIdKey(String sessionId);

    public void setUserIdSessionDataKey(User user, String session);

    public String getUserIdSessionDataKey(String userId);

    public Boolean existUserIdSessionDataKey(String s);

    public void addKickSessionKey(String oldSessionId);

    public Boolean existKickSessionKey(String oldSessionId);

    public void clearKickSessionKey(String oldSessionId);
}
