package com.autobiography.resources;

import com.autobiography.model.db.AutoBioTextType;
import com.autobiography.model.db.Person;
import com.autobiography.views.AutobiographyReadVew;
import com.autobiography.views.GenericView;
import com.autobiography.views.ResetPasswordView;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Author Dmitriy Liandres
 * Date 25.10.2015
 */
@Path("/ajax")
@Produces(MediaType.TEXT_HTML + ";charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class AjaxViewResource {


    @GET
    @Path("login")
    public GenericView getLoginView() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() && ((Person) subject.getPrincipal()).getId() != null) {
            return getProfileView();
        } else {
            return new GenericView(GenericView.LOGIN_FTL);
        }
    }

    @GET
    @Path("logout")
    public GenericView logoutView() {
        SecurityUtils.getSubject().logout();
        return getLoginView();
    }

    @GET
    @Path("resetPassword")
    public GenericView resetPasswordView() {
        return new ResetPasswordView(null);
    }


    @GET
    @Path("profile")
    public GenericView getProfileView() {
        return new GenericView(GenericView.PROFILE_FTL);
    }

    @GET
    @Path("profile-read")
    public GenericView getProfileRadView() {
        return new GenericView(GenericView.PROFILE_READ_FTL);
    }

    @GET
    @Path("autobiography-full")
    public GenericView getAutobiographyFull() {
        return new GenericView(GenericView.AUTOBIOGRAPHY_FULL_FTL);
    }

    @GET
    @Path("autobiography-full-read")
    public GenericView getAutobiographyFullRead() {
        return new AutobiographyReadVew(GenericView.AUTOBIOGRAPHY_READ_FTL, AutoBioTextType.FULL);
    }


    @GET
    @Path("not-found")
    public GenericView getNotFoundView() {
        return new GenericView(GenericView.NOT_FOUND_FTL);
    }

    @GET
    @Path("not-authorized")
    public GenericView getNotAuthorizedView() {
        return new GenericView(GenericView.NOT_AUTHORIZED_FTL);
    }

    @GET
    @Path("autobiography-for-work")
    public GenericView getAutobiographyForWork() {
        return new GenericView(GenericView.AUTOBIOGRAPHY_FOR_WORK_FTL);
    }

    @GET
    @Path("autobiography-for-work-read")
    public GenericView getAutobiographyForWorkRead() {
        return new AutobiographyReadVew(GenericView.AUTOBIOGRAPHY_READ_FTL, AutoBioTextType.FOR_WORK);
    }

    @GET
    @Path("autobiography-interesting-read")
    public GenericView getAutobiographyInterestingRead() {
        return new AutobiographyReadVew(GenericView.AUTOBIOGRAPHY_READ_FTL, AutoBioTextType.INTERESTING);
    }

    @GET
    @Path("autobiography-interesting")
    public GenericView getAutobiographyInteresting() {
        return new GenericView(GenericView.AUTOBIOGRAPHY_INTERESTING_FTL);
    }

    @GET
    @Path("about")
    public GenericView getAbout() {
        return new GenericView(GenericView.ABOUT_FTL);
    }

    @GET
    @Path("contact")
    public GenericView getContact() {
        return new GenericView(GenericView.CONTACT_FTL);
    }


    @GET
    @Path("all")
    public GenericView getAll() {
        return new GenericView(GenericView.ALL_FTL);
    }




}
