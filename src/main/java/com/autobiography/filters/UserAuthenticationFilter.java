package com.autobiography.filters;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Author Dmitriy Liandres
 * Date 25.10.2015
 */
public class UserAuthenticationFilter implements Filter {

    @SuppressWarnings("unused")
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        servletRequest.setCharacterEncoding("UTF-8");
//        System.out.println("Default Charset=" + Charset.defaultCharset());
//        System.out.println("file.encoding=" + System.getProperty("file.encoding"));
//        HttpServletRequest httpServletRequest = ((HttpServletRequest) servletRequest);
//        String path = httpServletRequest.getRequestURI();
//        if("/data/profile".equals(path) && "POST".equalsIgnoreCase(httpServletRequest.getMethod())){
//            String body = servletRequest.getReader().lines()
//                    .reduce("", (accumulator, actual) -> accumulator + actual);
//            System.out.println("BODY = " + body);
//        }
//        boolean isForwardToBaseNotNeeded =
//                path.startsWith("/ajax")
//                        || (path.equals("/login") && ((HttpServletRequest) servletRequest).getMethod().equals("POST"))
//                        || path.equals("/")
//                        || path.startsWith("/assets");
//        if (!(isForwardToBaseNotNeeded)) {
//            forwardToSupportedUrl(servletRequest, servletResponse);
//        }
//        //todo let's do not filter for now

        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(null, "");
            token.setRememberMe(false);
            currentUser.login(token);
        }


        filterChain.doFilter(servletRequest, servletResponse);
//        int responseStatus = ((Response) servletResponse).getStatus();
//        if( responseStatus == javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode()
//                || responseStatus == javax.ws.rs.core.Response.Status.FORBIDDEN.getStatusCode()){
        //   forwardToLogin(servletRequest, servletResponse);
        // }
        //

    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    //Forward the request to / resource to make a client redirect to login page
    private void forwardToLogin(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        request.getRequestDispatcher("/").forward(servletRequest, servletResponse);
    }


}
