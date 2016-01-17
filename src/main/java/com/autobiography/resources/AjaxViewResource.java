package com.autobiography.resources;

import com.autobiography.shiro.GeneralDomainPermission;
import com.autobiography.shiro.PermissionActionType;
import com.autobiography.shiro.PermissionObjectType;
import com.autobiography.views.GenericView;
import org.apache.shiro.SecurityUtils;

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
@Produces(MediaType.TEXT_HTML + "; charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON)
public class AjaxViewResource {


    @GET
    @Path("login")
    public GenericView getLoginView() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return getMainView();
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
    @Path("main")
    public GenericView getMainView() {
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.VIEW));
        return new GenericView(GenericView.MAIN_FTL);
    }

    @GET
    @Path("profile")
    public GenericView getProfileView() {
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.VIEW));
        return new GenericView(GenericView.PROFILE_FTL);
    }

    @GET
    @Path("autobiography-full")
    public GenericView getAutobiographyFull() {
        SecurityUtils.getSubject().checkPermission(new GeneralDomainPermission(PermissionObjectType.PROFILE, PermissionActionType.VIEW));
        return new GenericView(GenericView.AUTOBIOGRAPHY_FULL_FTL);
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
}
