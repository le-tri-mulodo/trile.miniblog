/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.mulodo.miniblog.common.Contants;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 *
 */
@Controller
@Path(Contants.URL_USER)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userSer;

    @Path(Contants.URL_ADD)
    @POST
    public Response add() {
	logger.info("add");
	return Response.status(200).entity("Hello").build();
    }

    @Path(Contants.URL_UPDATE)
    @PUT
    public Response update() {
	logger.info("update");
	return Response.status(200).entity("Hello").build();
    }

    @Path(Contants.URL_SEARCH)
    @GET
    public Response search(@PathParam(value = "querry") String querry) {
	logger.info("search {}", querry);
	return Response.status(200).entity("Hello").build();
    }

    @Path(Contants.URL_GET_BY_ID)
    @GET
    public Response getById(@PathParam(value = "id") int userID) {
	logger.info("UserID {}", userID);
	return Response.status(200).entity("Hello").build();
    }

    @Path(Contants.URL_CHPWD)
    @PUT
    public Response changePassword() {
	logger.info("changePassword");
	return Response.status(200).entity("Hello").build();
    }
}
