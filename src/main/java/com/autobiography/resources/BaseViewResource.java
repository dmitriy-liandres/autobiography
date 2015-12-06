package com.autobiography.resources;

import com.autobiography.core.Person;
import com.autobiography.db.PersonDAO;
import com.autobiography.shiro.GeneralDomainPermission;
import com.autobiography.shiro.PermissionObjectType;
import com.autobiography.views.BaseView;
import com.google.inject.Inject;
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

    private PersonDAO personDAO;

    @Inject
    public BaseViewResource(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public BaseView getMainView() {
        return new BaseView();
    }


    @GET
    @Path("{urlPath}")
    @Produces(MediaType.TEXT_HTML)
    public BaseView profileView(@PathParam("urlPath") String urlPath) {
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, "view"));
        return new BaseView();
    }


    @GET
    @Path("logout")
    @Produces(MediaType.TEXT_HTML)
    public Response logoutView() {
        SecurityUtils.getSubject().logout();
        URI uri = UriBuilder.fromUri("/").build();
        return Response.seeOther(uri).build();
    }

    @POST
    @Path("register")
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public Response registerView(@FormParam("email") String email,
                                 @FormParam("password") String password,
                                 @FormParam("rememberMe") @DefaultValue("false") Boolean rememberMe) {
        Person person = personDAO.findByUsername(email);
        if (person != null) {
//error
            URI uri = UriBuilder.fromUri("/").build();
            return Response.seeOther(uri).build();
        } else {
            person = new Person();
            person.setPassword(password);
            person.setUsername(email);
            personDAO.create(person);
        }
        return loginView(email, password, rememberMe);
    }


    @POST
    @Path("login")
    @Produces(MediaType.TEXT_HTML)
    @UnitOfWork
    public Response loginView(@FormParam("email") String email,
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
