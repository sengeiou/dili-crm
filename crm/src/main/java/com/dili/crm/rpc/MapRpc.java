package com.dili.crm.rpc;

import com.alibaba.fastjson.JSONObject;
import com.dili.http.okhttp.OkHttpUtils;
import com.dili.ss.util.ExportUtils;
import okhttp3.MediaType;
import okhttp3.Response;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 地图Web服务API
 * Created by asiam on 2018/1/3 0003.
 */
@Component
public class MapRpc {

    public final static Logger log = Logger.getLogger(MapRpc.class);

    /**
     * 正/逆地理编码服务<br/>
     * 示例:
     * Map param = new HashMap();
     *    param.put("ak", "...");
     *    param.put("location", "39.934,116.329");
     *    param.put("output", "json");
     *   mapRpc.geocoder(param)
     * @param param
     * @return
     * @throws IOException
     */
    public String geocoder(Map param) throws IOException {
        Response resp = OkHttpUtils
                .post()
                .url("http://api.map.baidu.com/geocoder/v2/")
                .params(param)
                .build()
                //10秒过期
                .connTimeOut(1000L*10L)
                .readTimeOut(1000L*10L)
                .writeTimeOut(1000L*10L)
                .execute();
        if(resp.isSuccessful()){
            return resp.body().string();
        }else{
            log.error(String.format("远程调用[geocoder]发生失败,code:[%s], message:[%s]", resp.code(),resp.message()));
            return resp.body().string();
        }
    }


}