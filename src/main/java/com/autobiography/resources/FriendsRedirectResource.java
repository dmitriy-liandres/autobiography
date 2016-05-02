package com.autobiography.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;


/**
 * This resource is used to redirect events sent by vk com to payment servlet of vk.com/app2179436
 * Thus it is not related to autobio itself
 * Author Dmitriy Liandres
 * Date 30.04.2016
 */
@Path("/friends/payment")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class FriendsRedirectResource {

    @POST
    public Response redirectToFriendsApplicationPost(@Context HttpServletRequest httpServletRequest) {
        Map<String, String[]> params = httpServletRequest.getParameterMap();
        String redirectUrl = "http://91.218.229.113:81/friends/payment";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(redirectUrl);
        Form form = new Form();
        for (Map.Entry<String, String[]> param : params.entrySet()) {
            form.param(param.getKey(), param.getValue()[0]);
        }
        String response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE),
                        String.class);
        return Response.status(Response.Status.OK).entity(response).build();
    }


    @GET
    public Response redirectToFriendsApplicationGet(@Context HttpServletRequest httpServletRequest) {
        return redirectToFriendsApplicationPost(httpServletRequest);
    }
}
