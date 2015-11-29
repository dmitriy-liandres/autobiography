package com.autobiography.resources;

import com.autobiography.shiro.GeneralDomainPermission;
import com.autobiography.shiro.PermissionObjectType;
import com.autobiography.views.BaseView;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Author Dmitriy Liandres
 * Date 25.10.2015
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class BaseViewResource {


    @GET
    @Produces(MediaType.TEXT_HTML)
    public BaseView getMainView() {
        return new BaseView();
    }


    @GET
    @Path("profile")
    @Produces(MediaType.TEXT_HTML)
    public BaseView getPersonViewFreemarker() {
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, "view"));
        return new BaseView();
    }


    @POST
    @Path("login")
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public Response getPersonViewFreemarker(@FormParam("email") String email,
                                            @FormParam("password") String password,
                                            @FormParam("rememberMe") @DefaultValue("false") Boolean rememberMe) {

        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(email, password);
        //this is all you have to do to support 'remember me' (no config - built in!):
        token.setRememberMe(rememberMe);
        currentUser.login(token);
        URI uri = UriBuilder.fromUri("/profile").build();
        return Response.seeOther(uri).build();
    }


}
