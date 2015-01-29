/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 *
 */
@Controller
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Autowired
    private UserService userSer;

    @Path("/")
    @GET
    public Response hello() {
	return Response.status(200).entity("Hello").build();
    }
}
