package org.loxf.metric.filter;

import org.apache.commons.lang.StringUtils;
import org.loxf.metric.common.dto.UserDto;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 会话管理拦截器
 * <filter>
 * <filter-name>LoginFilter</filter-name>
 * <filter-class>org.loxf.metric.filter.LoginFilter</filter-class>
 * <init-param>
 * <param-name>SECURITY_URI</param-name>
 * <param-value>/admin/*,/security/*</param-value>
 * </init-param>
 * </filter>
 * <filter-mapping>
 * <filter-name>LoginFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 */
public class LoginFilter implements Filter {
    private String[] security_uri_array;

    private final static String SESSION_NAME = "LOXFSESSION";

    private String LOGIN_PAGE = "/tologin.html";

    public LoginFilter() {
    }

    public void destroy() {
    }

    /**
     * 初始化需要拦截的请求URL
     *
     * @param fConfig
     * @throws ServletException
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // 获取需要登录验证的URI
        String security_uri = fConfig.getInitParameter("SECURITY_URI");
        security_uri = StringUtils.isNotBlank(security_uri) ? security_uri : "";
        // URI以逗号分隔
        security_uri_array = security_uri.split(",");
        //错误页面
        LOGIN_PAGE = fConfig.getInitParameter("LOGIN_PAGE");
    }

    /**
     * session拦截
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        // 设置编码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        boolean needSecurity = false;
        // 判断请求URI是否在配置的需要登录验证的URI列表中
        for (int i = 0; i < security_uri_array.length; i++) {
            String sUri = security_uri_array[i];
            if ("".equals(sUri))
                continue;
            if (req.getRequestURI().startsWith(
                    req.getContextPath() + sUri.replace("*", ""))) {
                needSecurity = true;
                break;
            }
        }
        //如果不需要拦截请求,PASS
        if (needSecurity) {
            //sesesion是否有效
            boolean userValid = checkUser(req, resp);
            if (!userValid) {
                resp.sendRedirect(req.getContextPath() + LOGIN_PAGE);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * 校验用户
     *
     * @param req
     * @return
     */
    private boolean checkUser(HttpServletRequest req, HttpServletResponse response) {
        String sessionId = getSessionId(req);
        if (StringUtils.isBlank(sessionId)) {
            return false;
        }
        //从redis中获取sessionId对应的信息
        UserDto value = (UserDto) req.getSession().getAttribute(sessionId);
        if (value != null) {
            String currentUserAgent = req.getHeader("User-Agent");
            String loginUserAgent = value.getUserAgent();
            if (loginUserAgent.equals(currentUserAgent)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static void setUser(HttpServletRequest request, String sessionId, UserDto user) {
        HttpSession session = request.getSession();
        session.setAttribute(sessionId, user);
    }

    /**
     * 添加sessionId到cookie中
     *
     * @param response
     * @param sessionId
     */
    public static void addSessionId(HttpServletResponse response, String sessionId) {
        Cookie cookie = new Cookie(SESSION_NAME, sessionId);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60);
        response.addCookie(cookie);
    }

    /**
     * 退出登陆
     *
     * @param response
     */
    public static void removeSessionId(HttpServletResponse response) {
        Cookie cookie = new Cookie(SESSION_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static String getSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        if (null == cookies) {
            return null;
        }
        for (Cookie c : cookies) {
            if (SESSION_NAME.equalsIgnoreCase(c.getName())) {
                cookie = c;
                break;
            }
        }
        //cookie不存在,session为空
        if (cookie == null) {
            return null;
        }
        return cookie.getValue();
    }

    public static UserDto getUser(HttpServletRequest request) {
        String sessionId = LoginFilter.getSessionId(request);
        if (StringUtils.isBlank(sessionId)) {
            return null;
        }

        UserDto value = (UserDto) request.getSession().getAttribute(sessionId);
        return value;
    }

}
