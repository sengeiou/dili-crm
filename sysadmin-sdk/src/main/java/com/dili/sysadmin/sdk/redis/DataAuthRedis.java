package com.dili.sysadmin.sdk.redis;

import com.alibaba.fastjson.JSONObject;
import com.dili.sysadmin.sdk.session.SessionConstants;
import com.dili.sysadmin.sdk.util.ManageRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据权限redis操作
 * Created by asiamaster on 2017/7/5.
 */
@Service
public class DataAuthRedis {
    @Autowired
    private ManageRedisUtil redisUtil;

    /**
     * 根据userId和数据权限type获取数据权限列表
     * @param type
     * @param userId
     * @return  DataAuth List<Map>
     */
    public List<Map> dataAuth(String type, Long userId) {
        BoundSetOperations<String, String> boundSetOperations = redisUtil.getRedisTemplate().boundSetOps (SessionConstants.USER_DATAAUTH_KEY + userId);
        List<Map> dataAuthMap = new ArrayList<>();
        if(boundSetOperations.size()<=0) {
            return dataAuthMap;
        }

        //根据类型过滤
        for(String dataAuthJson : boundSetOperations.members()) {
            JSONObject dataAuth = JSONObject.parseObject(dataAuthJson);
            if(dataAuth.get("type").equals(type)){
                dataAuthMap.add(dataAuth);
            }
        }
        return dataAuthMap;
    }

    /**
     * 指定Key的当前数据权限DataAuth的Map
     * @param userId
     * @param type
     * @return
     */
    public Map currentdataAuth(Long userId, String type) {
        String key = userId + ":" + type;
        String json = redisUtil.get(SessionConstants.USER_CURRENT_KEY + key, String.class);
        return JSONObject.parseObject(json);
    }
}
