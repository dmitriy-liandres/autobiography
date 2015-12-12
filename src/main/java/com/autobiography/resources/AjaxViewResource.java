package com.autobiography.resources;

import com.autobiography.shiro.GeneralDomainPermission;
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
@Produces(MediaType.TEXT_HTML)
@Consumes(MediaType.APPLICATION_JSON)
public class AjaxViewResource {


    @GET
    @Path("login")
    public GenericView getLoginView() {
        return new GenericView(GenericView.LOGIN_FTL);
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
        return new GenericView(GenericView.MAIN_FTL);
    }

    @GET
    @Path("profile")
    public GenericView getProfileView() {
        return new GenericView(GenericView.PROFILE_FTL);
    }
}
