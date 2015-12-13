package com.autobiography.providers;

import com.google.inject.Inject;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Author Dmitriy Liandres
 * Date 22.11.2015
 */
@Provider
public class AnyExceptionMapper implements ExceptionMapper<Throwable> {
    private URI startPageUri;
    private URI notAuthorizedPageUri;
    private javax.inject.Provider<HttpServletRequest> requestProvider;

    @Inject
    public AnyExceptionMapper(javax.inject.Provider<HttpServletRequest> requestProvider) throws URISyntaxException {
        this.startPageUri = new URI("/");
        //todo set correct url
        this.notAuthorizedPageUri = new URI("/ajax/not-authorized");
        this.requestProvider = requestProvider;
    }

    public Response toResponse(Throwable ex) {
        //next param defines whether data requested or page code
        boolean isDataAjaxRequest = requestProvider.get().getRequestURI().startsWith("/data");
        URI redirectUri;
        Response.Status status = Response.Status.OK;
        if (ex instanceof UnauthenticatedException) {
            //401 Unauthorized response should be used for missing or bad authentication,
            redirectUri = notAuthorizedPageUri;
            status = Response.Status.UNAUTHORIZED;
        } else if (ex instanceof UnauthorizedException) {
            //403 Forbidden response should be used afterwards,
            //when the user is authenticated but isn’t authorized to perform the requested operation on the given resource.
            redirectUri = startPageUri;
            status = Response.Status.FORBIDDEN;
        } else {
            ex.printStackTrace();
            redirectUri = startPageUri;
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return Response.status(status).build();
//        if (isDataAjaxRequest) {
//            return Response.status(status).build();
//        } else {
//            return Response.seeOther(redirectUri).build();
//        }

    }


}
