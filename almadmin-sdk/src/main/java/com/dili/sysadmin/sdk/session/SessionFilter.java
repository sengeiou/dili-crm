package com.dili.sysadmin.sdk.session;


import com.dili.sysadmin.sdk.domain.UserTicket;
import com.dili.sysadmin.sdk.exception.NotAccessPermission;
import com.dili.sysadmin.sdk.exception.NotLoginException;
import com.dili.sysadmin.sdk.exception.RedirectException;
import com.dili.sysadmin.sdk.redis.UserUrlRedis;
import com.dili.sysadmin.sdk.util.WebContent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限统一拦截器
 */
@Component
public class SessionFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SessionFilter.class);
    //配置信息
    @Autowired
    private ManageConfig config;

    private FilterConfig filterConfig;

    @Autowired
    private UserUrlRedis userResRedis;

    public void destroy() {}

    public void init(FilterConfig conf) throws ServletException {
        filterConfig = conf;
//        userResRedis = SpringUtil.getBean(UserResRedis.class);
//        config = SpringUtil.getBean(ManageConfig.class);
    }


    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filter) throws IOException, ServletException {
        WebContent.resetLocal();
        SessionContext.resetLocal();
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        WebContent.put(req);
        WebContent.put(resp);
        PermissionContext pc = new PermissionContext(req, resp, this, config);
        WebContent.put(SessionConstants.MANAGE_PERMISSION_CONTEXT, pc);
        //判断是否要(include或exclude)权限检查, 不过滤就直接放过
        if (!config.hasChecked()) {
            filter.doFilter(req, resp);
            return;
        }
        proxyHandle(req, resp, filter);
    }

    private void proxyHandle(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws IOException {
        WebContent.put(request);
        WebContent.put(response);
        PermissionContext pc = (PermissionContext) WebContent.get(SessionConstants.MANAGE_PERMISSION_CONTEXT);
        if (log.isDebugEnabled()) {
            log.debug("请求地址:" + pc.getUrl());
        }
        try {
            //iframe
            checkIframe(pc);
            //登录
            checkUser(pc);
            filter.doFilter(request, response);
        } catch (RedirectException e) {
            pc.sendRedirect(e.getPath());
        } catch (NotLoginException e) {
            pc.noAccess();
        } catch (NotAccessPermission e) {
            if (log.isInfoEnabled()) {
                log.info("用户{Session:" + pc.getSessionId() + ", userId:" + pc.getUserId() + "}没有访问" + pc.getUrl() + "权限！");
            }
            pc.nonPermission();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            SessionContext.remove();
        }
    }

    /**
     * 检查是否在iframe中
     * @param pc
     */
    private void checkIframe(PermissionContext pc){
        String referer = pc.getReferer();
        if (StringUtils.equals("/main.do", pc.getUri())) {
            return;
        }
        if (!pc.getConfig().getMustIframe()) {
            return;
        }
        if (!StringUtils.isBlank(referer)) {
            return;
        }
        if(pc.getHandler().getClass().getName().equalsIgnoreCase("ResourceHttpRequestHandler")){
            return;
        }
        StringBuffer path = new StringBuffer();
        path.append("/main.do?returnUrl=");
        path.append(pc.getUrl());
        path.append("?");
        path.append(pc.getQueryString());
        throw new RedirectException(path.toString());
    }

    /**
     * 检查用户是否有权限访问
     * @param pc
     */
    private void checkUser(PermissionContext pc) {
        if (pc.getSessionId() == null) {
            throw new NotLoginException();
        }
        UserTicket user = pc.getUser();
        if (user == null) {
            throw new NotLoginException();
        }
        if (userResRedis.checkUserMenuUrlRight(user.getId(), pc.getUrl())) {
            return;
        }
        //检测授权
        UserTicket auth = pc.getAuthorizer();
        if(auth == null){
            throw new NotAccessPermission();
        }
        throw new NotAccessPermission();
    }

}
