package com.autobiography.resources;

import com.autobiography.views.BaseView;
import com.google.inject.Inject;
import org.apache.shiro.authz.annotation.RequiresPermissions;

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

    @Inject
    private AjaxViewResource ajaxViewResource;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public BaseView getMainView() {
        return new BaseView();
    }


    @GET
    @Path("profile")
    @Produces(MediaType.TEXT_HTML)

    @RequiresPermissions("account:create")
    public BaseView getPersonViewFreemarker() {
        return new BaseView();
    }


    @POST
    @Path("login")
    @Produces(MediaType.TEXT_HTML)
    public Response getPersonViewFreemarker(@FormParam("username") String username,
                                            @FormParam("password") String password) {
        URI uri = UriBuilder.fromUri("/profile").build();
        return Response.seeOther(uri).build();
    }


}
