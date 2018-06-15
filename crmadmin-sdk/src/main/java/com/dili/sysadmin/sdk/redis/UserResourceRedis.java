package com.dili.sysadmin.sdk.redis;

import com.dili.sysadmin.sdk.exception.NotLoginException;
import com.dili.sysadmin.sdk.session.SessionConstants;
import com.dili.sysadmin.sdk.util.ManageRedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户资源redis操作
 * Created by asiamastor on 2018/5/24.
 */
@Service
public class UserResourceRedis {
    private static final Logger log = LoggerFactory.getLogger(UserResourceRedis.class);

    @Autowired
    private ManageRedisUtil redisUtil;

    /**
     * 从redis根据用户id判断是否有resourceCode的访问权限
     * @param userId
     * @param resourceCode
     * @return
     */
    public boolean checkUserResourceRight(Long userId, String resourceCode) {
        if (userId == null) {
            log.debug("失败加载用户信息!");
            throw new NotLoginException();
        }
        return checkRedisUserResource(userId, resourceCode);

    }

    /**
     * 从redis根据用户id判断是否有resourceCode的访问权限
     * @param userId
     * @param resourceCode
     * @return
     */
    private boolean checkRedisUserResource(Long userId, String resourceCode) {
        return isMemberKey(SessionConstants.USER_RESOURCE_CODE_KEY + userId.toString(), resourceCode);
    }

    /**
     * 判断redis的map中的key value匹配
     * @param key
     * @param value
     * @return
     */
    private Boolean isMemberKey(String key, Object value){
        return redisUtil.getRedisTemplate().boundSetOps(key).isMember(value);
    }
}
