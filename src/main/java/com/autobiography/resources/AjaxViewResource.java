package com.autobiography.resources;

import com.autobiography.views.LoginView;
import com.autobiography.views.ProfileView;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Author Dmitriy Liandres
 * Date 25.10.2015
 */
@Path("/ajax")
@Produces(MediaType.APPLICATION_JSON)
public class AjaxViewResource {
    @GET
    @Path("login")
    @Produces(MediaType.TEXT_HTML)
    public LoginView getLoginView() {
        return new LoginView();
    }

    @GET
    @Path("profile")
    @Produces(MediaType.TEXT_HTML)
    public ProfileView getProfileView() {
        return new ProfileView();
    }

}
