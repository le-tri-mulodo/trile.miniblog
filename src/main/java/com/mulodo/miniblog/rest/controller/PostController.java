/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.mulodo.miniblog.common.Contants;

/**
 * @author TriLe
 */
@Controller
@Path(Contants.URL_POST)
@Produces(MediaType.APPLICATION_JSON)
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Path(Contants.URL_ADD)
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response add(@FormParam("token") String token) {
        return Response.status(200).build();
    }

    @Path(Contants.URL_UPDATE)
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response update(@FormParam("token") String token) {
        return Response.status(200).build();
    }

    @Path(Contants.URL_DELETE)
    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response delete(@FormParam("token") String token) {
        return Response.status(200).build();
    }

    @Path(Contants.URL_PUBLICT)
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response activeDeactive(@FormParam("token") String token) {
        return Response.status(200).build();
    }

    @Path(Contants.URL_GET)
    @GET
    public Response allPost() {
        return Response.status(200).build();
    }

    @Path(Contants.URL_TOP)
    @GET
    public Response topPost() {
        return Response.status(200).build();
    }

    @Path(Contants.URL_TOP)
    @GET
    public Response getById(@FormParam("token") String token) {
        return Response.status(200).build();
    }

    @Path(Contants.URL_GET_BY_USER)
    @GET
    public Response getByUser(@PathParam("user_id") int UserID) {
        return Response.status(200).build();
    }

    @Path(Contants.URL_SEARCH)
    @GET
    public Response search(@PathParam("query") String query) {
        return Response.status(200).build();
    }
}
