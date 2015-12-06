package com.autobiography.resources;

import com.autobiography.db.PersonDAO;
import com.autobiography.model.db.Person;
import com.autobiography.views.GenericView;
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
    public GenericView getMainView() {
        return new GenericView(GenericView.BASE_FTL);
    }


    @GET
    @Path("{urlPath}")
    @Produces(MediaType.TEXT_HTML)
    public GenericView profileView(@PathParam("urlPath") String urlPath) {
        return getMainView();
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
        token.setRememberMe(rememberMe);
        currentUser.login(token);
        URI uri = UriBuilder.fromUri("/main").build();
        return Response.seeOther(uri).build();
    }

}
