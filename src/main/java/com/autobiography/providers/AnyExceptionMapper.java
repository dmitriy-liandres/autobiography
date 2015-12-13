package com.autobiography.providers;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Author Dmitriy Liandres
 * Date 22.11.2015
 */
@Provider
public class AnyExceptionMapper implements ExceptionMapper<Throwable> {

    public Response toResponse(Throwable ex) {
        //next param defines whether data requested or page code
        Response.Status status;
        if (AuthenticationException.class.isAssignableFrom(ex.getClass())) {
            //401 Unauthorized response should be used for missing or bad authentication,
            status = Response.Status.UNAUTHORIZED;
        } else if (AuthorizationException.class.isAssignableFrom(ex.getClass())) {
            //403 Forbidden response should be used afterwards,
            //when the user is authenticated but isn’t authorized to perform the requested operation on the given resource.
            status = Response.Status.FORBIDDEN;
        } else {
            ex.printStackTrace();
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return Response.status(status).build();

    }


}
