/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.mulodo.miniblog.common.Contants;
import com.mulodo.miniblog.common.Util;
import com.mulodo.miniblog.message.ResultMessage;
import com.mulodo.miniblog.message.SuccessMessage;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 *
 */
@Controller
@Path(Contants.URL_USER)
@Produces(MediaType.APPLICATION_JSON)
@ValidateRequest
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userSer;

    @Path(Contants.URL_ADD)
    @POST
    @ValidateRequest
    public Response add(@NotNull(message = "username is required") @FormParam(value = "username") String username,
	    @NotNull(message = "password is required") @FormParam(value = "password") String password,
	    @NotNull(message = "firstname is required") @FormParam(value = "firstname") String firstname,
	    @NotNull(message = "lastname is required") @FormParam(value = "lastname") String lastname,
	    @FormParam(value = "avatarlink") String avatarlink) {

	logger.debug("Add new user");

	User user = new User();
	// Set username
	user.setUserName(username);
	// Hash and set password
	user.setPassHash(Util.hashSHA256(password));
	// Set firstname
	user.setFirstName(firstname);
	// Set lastname
	user.setLastName(lastname);
	// Set Avatarlink
	user.setAvatarLink(avatarlink);

	// Call user service
	user = userSer.add(user);

	SuccessMessage successMsg = new SuccessMessage(201, "Create user success!");
	ResultMessage result = new ResultMessage(successMsg, user);
	return Response.status(200).entity(result).build();
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
