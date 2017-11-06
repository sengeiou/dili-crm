package com.dili.sysadmin.sdk.redis;

import com.dili.sysadmin.sdk.exception.NotLoginException;
import com.dili.sysadmin.sdk.session.SessionConstants;
import com.dili.sysadmin.sdk.util.ManageRedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户和菜单URL redis操作
 * Created by asiamastor on 2017/7/5.
 */
@Service
public class UserUrlRedis {
    private static final Logger log = LoggerFactory.getLogger(UserUrlRedis.class);

    @Autowired
    private ManageRedisUtil redisUtil;

    /**
     * 根据用户ID判断访问url的权限
     * @param userId
     * @param url
     * @return
     */
    public boolean checkUserMenuUrlRight(Long userId, String url) {
        if (userId == null) {
            log.debug("失败加载用户信息!");
            throw new NotLoginException();
        }
        // 去掉http和https前缀, 判断用户权限
        return checkRedisUserMenuUrl(userId, url.trim().replace("http://", "").replace("https://", ""));

    }

    /**
     * 从redis根据用户id判断是否有菜单URL的访问权限
     *
     * @param userId
     * @param menuUrl
     * @return
     */
    private boolean checkRedisUserMenuUrl(Long userId, String menuUrl) {
        return isMemberKey(SessionConstants.USER_MENU_URL_KEY + userId.toString(), menuUrl);
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
