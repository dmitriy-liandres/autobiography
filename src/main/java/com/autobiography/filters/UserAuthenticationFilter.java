package com.autobiography.filters;

import org.eclipse.jetty.server.Response;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
//        String path = ((HttpServletRequest) servletRequest).getRequestURI();
//        boolean isForwardToBaseNotNeeded =
//                path.startsWith("/ajax")
//                        || (path.equals("/login") && ((HttpServletRequest) servletRequest).getMethod().equals("POST"))
//                        || path.equals("/")
//                        || path.startsWith("/assets");
//        if (!(isForwardToBaseNotNeeded)) {
//            forwardToSupportedUrl(servletRequest, servletResponse);
//        }
//        //todo let's do not filter for now

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
