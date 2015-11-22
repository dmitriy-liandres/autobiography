package com.autobiography.providers;

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
    private URI errorPageUri;

    public AnyExceptionMapper() throws URISyntaxException {
        this.startPageUri = new URI("/");
        //todo set correct url
        this.errorPageUri = new URI("/error");
    }

    public Response toResponse(Throwable ex) {
        URI redirectUri;
        switch (ex.getClass().getSimpleName()) {
            case "UnauthenticatedException":
            case "AuthenticationException":
                redirectUri = startPageUri;
                break;
            default:
                redirectUri = errorPageUri;
        }

        return Response.temporaryRedirect(redirectUri).
                build();
    }


}
