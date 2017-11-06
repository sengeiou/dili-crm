package com.dili.sysadmin.sdk.service;

import com.dili.sysadmin.sdk.session.PermissionContext;
import com.dili.sysadmin.sdk.session.SessionConstants;
import com.dili.sysadmin.sdk.session.SessionContext;
import com.dili.sysadmin.sdk.util.WebContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jk on 2017/5/11.
 */
public class AuthDataApiService extends AbstractApiService {
    private static final Logger log = LoggerFactory.getLogger(AuthDataApiService.class);
    public AuthDataApiService(String token, String baseUrl) {
        super(token, baseUrl);
    }

    public void refreshAuthData(String type){
        try {
            PermissionContext pc = (PermissionContext) WebContent.get(SessionConstants.MANAGE_PERMISSION_CONTEXT);

            Map<String, String> param = new HashMap<>();
            param.put("type", type);
            param.put("userId", SessionContext.getSessionContext().getUserTicket().getId().toString());
            param.put(SessionConstants.SESSION_ID, pc.getSessionId());
            httpGet("/dataAuth/refreshAuthData.do", param);
        } catch (IOException e) {
            log.info("刷新数据权限出现异常!", e);
        }
    }

//    public static void main(String[] args) {
//        AuthDataApiService adas = new AuthDataApiService("", "http://mg.nong12.com");
//        adas.refreshAuthData("department");
//    }
}
