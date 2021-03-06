package com.autobiography.resources;

import com.autobiography.db.PersonDAO;
import com.autobiography.db.ProfileDAO;
import com.autobiography.helpers.EmailHelper;
import com.autobiography.model.db.Person;
import com.autobiography.views.GenericView;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Locale;

/**
 * Author Dmitriy Liandres
 * Date 25.10.2015
 */
@Path("/")
@Produces(MediaType.TEXT_HTML + ";charset=UTF-8")
public class BaseViewResource {

    private PersonDAO personDAO;
    private ProfileDAO profileDAO;

    @Inject
    public BaseViewResource(PersonDAO personDAO, ProfileDAO profileDAO) {
        this.personDAO = personDAO;
        this.profileDAO = profileDAO;

    }

    @GET
    public GenericView getMainView(@QueryParam("lang") String lang) {
        if (lang != null) {
            switch (lang) {
                case "ru":
                    Locale.setDefault(new Locale.Builder().setLanguage("ru").setRegion("RU").build());
                    break;
                default:
                    Locale.setDefault(Locale.US);
                    break;
            }
        }

        return new GenericView(GenericView.BASE_FTL);
    }


    @GET
    @Path("{urlPath}")
    public GenericView profileView(@PathParam("urlPath") String urlPath) {
        return getMainView(null);
    }

    @GET
    @Path("reset-password")
    public GenericView resetPasswordView() {
        return getMainView(null);
    }


    @GET
    @Path("{urlPath}/{urlPath2}")
    public GenericView anyView(@PathParam("urlPath") String urlPath) {
        return getMainView(null);
    }


    @POST
    @Path("register")
    @UnitOfWork
    public Response registerView(@FormParam("email") String email,
                                 @FormParam("password") String password,
                                 @FormParam("rememberMe") @DefaultValue("false") Boolean rememberMe,
                                 @Nullable @QueryParam("redir") String redir) {
        Person person = personDAO.findByUsername(email);
        if (person != null) {
//error
            URI uri = UriBuilder.fromUri("/?duplicateLogin=true").build();
            return Response.seeOther(uri).build();
        } else {
            person = new Person();
            person.setPassword(password);
            person.setUsername(email);
            personDAO.create(person);
        }
        return loginView(email, password, rememberMe, redir);
    }


    @POST
    @Path("login")
    @UnitOfWork
    public Response loginView(@FormParam("email") String email,
                              @FormParam("password") String password,
                              @FormParam("rememberMe") @DefaultValue("false") Boolean rememberMe,
                              @Nullable @QueryParam("redir") String redir) {

        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(email, password);
        token.setRememberMe(rememberMe);
        String redirectUrl = StringUtils.isNoneEmpty(redir) ? redir : "";
        try {
            currentUser.login(token);
        } catch (Exception e) {
            return Response.seeOther(UriBuilder.fromUri("/?e=1&redir=" + redirectUrl).build()).entity("").build();
        }

        URI uri = UriBuilder.fromUri(redirectUrl).build();
        Person principal = (Person) SecurityUtils.getSubject().getPrincipal();
        SecurityUtils.getSubject().getSession().setAttribute("profile", profileDAO.findById(principal.getId()));
        return Response.seeOther(uri).build();
    }

    @POST
    @Path("reset-password")
    @UnitOfWork
    public Response resetPassword(@FormParam("email") String email) throws Exception {
        Person person = personDAO.findByUsername(email);
        if (person == null) {
            return Response.seeOther(UriBuilder.fromUri("/reset-password?e=incorrectEmail").build()).build();
        } else {
            EmailHelper.sendEmail("Your password for http://toolformoney.com/", "Your password for http://toolformoney.com/ is " + person.getPassword(), email);
        }
        URI uri = UriBuilder.fromUri("").build();
        return Response.seeOther(uri).build();
    }

}
