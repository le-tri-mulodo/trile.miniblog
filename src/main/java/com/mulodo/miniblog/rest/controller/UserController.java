/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.HibernateException;
import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.mulodo.miniblog.common.Contants;
import com.mulodo.miniblog.message.ResultMessage;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.TokenService;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 */
@Controller
@Path(Contants.URL_USER)
@Produces(MediaType.APPLICATION_JSON)
@ValidateRequest
public class UserController
{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userSer;
    @Autowired
    private TokenService tokenSer;

    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_ADD)
    @POST
    @ValidateRequest
    public Response add(
            @NotNull(message = "{username.NotNull}")
            @Pattern(regexp = Contants.WORDS_VALID_REGEX, message = "{username.Invalid}")
            @Size(min = 4, max = 64, message = "{username.Size}")
            @FormParam(value = "username")
            String username,

            @NotNull(message = "{password.NotNull}")
            @Size(min = 4, max = 999, message = "{password.Size}")
            @FormParam(value = "password")
            String password,

            @NotNull(message = "{firstname.NotNull}")
            @Pattern(regexp = Contants.WORDS_VALID_REGEX, message = "{firstname.Invalid}")
            @Size(min = 1, max = 64, message = "{firstname.Size}")
            @FormParam(value = "firstname")
            String firstname,

            @NotNull(message = "{lastname.NotNull}")
            @Pattern(regexp = Contants.WORDS_VALID_REGEX, message = "{lastname.Invalid}")
            @Size(min = 1, max = 64, message = "{lastname.Size}")
            @FormParam(value = "lastname")
            String lastname,

            @Size(min = 1, max = 256, message = "{avatarlink.Size}")
            @FormParam(value = "avatarlink")
            String avatarlink)
    {

        // Check username existed in db
        if (userSer.checkUserNameExist(username)) {
            // Log
            logger.info("Username [{}] existed", username);

            ResultMessage errorMsg = new ResultMessage(1001, "Username existed", "Username existed");

            return Response.status(400).entity(errorMsg).build();
        }

        User user = new User();
        // Set username
        user.setUserName(username);
        // Set password
        user.setPassHash(password);
        // Set firstname
        user.setFirstName(firstname);
        // Set lastname
        user.setLastName(lastname);
        // Set Avatarlink
        user.setAvatarLink(avatarlink);

        // Call user service to insert into db
        try {
            user = userSer.add(user);
        } catch (HibernateException e) {
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(9001, "Database access error",
                    String.format("Database error: %s", e.getMessage()));
            return Response.status(500).entity(dbErrMsg).build();
        }

        // Response success
        ResultMessage<User> resultMsg = new ResultMessage<User>(201, "Create user success!", user);
        return Response.status(201).entity(resultMsg).build();
    }

    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_UPDATE)
    @PUT
    public Response update(
            @NotNull(message = "{user_id.NotNull}")
            @FormParam(value = "user_id")
            @Min(value = 0)
            Integer user_id,

            @Pattern(regexp = Contants.WORDS_VALID_REGEX, message = "{firstname.Invalid}")
            @Size(min = 1, max = 64, message = "{firstname.Size}")
            @FormParam(value = "firstname")
            String firstname,

            @Pattern(regexp = Contants.WORDS_VALID_REGEX, message = "{lastname.Invalid}")
            @Size(min = 1, max = 64, message = "{lastname.Size}")
            @FormParam(value = "lastname")
            String lastname,

            @Size(min = 1, max = 256, message = "{avatarlink.Size}")
            @FormParam(value = "avatarlink")
            String avatarlink,

            @NotNull(message = "{token.NotNull}")
            @Size(min = 64, max = 64, message = "{token.Size}")
            @FormParam(value = "token")
            String token)
    {

        // Check token
        if (!tokenSer.checkToken(user_id, token)) {
            ResultMessage unauthorizedMsg = new ResultMessage(1001,
                    "Token in request invaild or expired", String.format(
                            "Token [%s] invaild or expired", token));
            return Response.status(401).entity(unauthorizedMsg).build();
        }

        // Create new user object to call user service
        User user = new User();
        // Set input
        user.setId(user_id);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setAvatarLink(avatarlink);
        // Update user
        try {
            user = userSer.update(user);
        } catch (HibernateException e) {
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(9001, "Database access error",
                    String.format("Database error: %s", e.getMessage()));
            return Response.status(500).entity(dbErrMsg).build();
        }

        // Response success
        ResultMessage<User> successMsg = new ResultMessage<User>(200, "Account updated success!",
                user);
        return Response.status(200).entity(successMsg).build();
    }

    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_SEARCH)
    @GET
    public Response search(
            @NotNull(message = "{query.NotNull}")
            @Size(min = 1, max = 64, message = "{query.Size}")
            @PathParam(value = "query")
            String query)
    {

        List<User> users = null;
        try {
            users = userSer.search(query);
        } catch (HibernateException e) {
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(9001, "Database access error",
                    String.format("Database error: %s", e.getMessage()));
            return Response.status(500).entity(dbErrMsg).build();
        }

        // Response success
        ResultMessage<List<User>> result = new ResultMessage<List<User>>(200, String.format(
                "Search success! %d results", users.size()), users);
        return Response.status(200).entity(result).build();
    }

    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_GET_BY_ID)
    @GET
    public Response getById(@PathParam(value = "id") int userId)
    {

        User user = null;
        try {
            user = userSer.get(userId);
        } catch (HibernateException e) {
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(9001, "Database access error",
                    String.format("Database error: %s", e.getMessage()));
            return Response.status(500).entity(dbErrMsg).build();
        }

        if (null == user) {
            // Response error
            ResultMessage userNotExistMsg = new ResultMessage(2001,
                    "User ID in request does not exist", String.format(
                            "User with id=%d does not exist", userId));
            return Response.status(400).entity(userNotExistMsg).build();
        }

        // Response success
        ResultMessage<User> result = new ResultMessage<User>(200, "Get user info success!", user);
        return Response.status(200).entity(result).build();
    }

    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_CHPWD)
    @PUT
    public Response changePassword(
            @NotNull(message = "{user_id.NotNull}")
            @FormParam(value = "user_id")
            @Min(value = 0)
            Integer user_id,

            @NotNull(message = "{password.NotNull}")
            @Size(min = 4, max = 999, message = "{password.Size}")
            @FormParam(value = "currentpassword")
            String currentpassword,

            @NotNull(message = "{newpassword.NotNull}")
            @Size(min = 4, max = 999, message = "{newpassword.Size}")
            @FormParam(value = "newpassword")
            String newpassword)
    {

        if (!userSer.checkPassword(user_id, currentpassword)) {
            // Response error
            ResultMessage invalidMsg = new ResultMessage(1002, "User id or password invalid",
                    "User id or password invalid");
            return Response.status(400).entity(invalidMsg).build();
        }

        // Call service to change password
        User user = null;
        try {
            user = userSer.changePassword(user_id, newpassword);
        } catch (HibernateException e) {
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(9001, "Database access error",
                    String.format("Database error: %s", e.getMessage()));
            return Response.status(500).entity(dbErrMsg).build();
        }

        // Response success
        ResultMessage<User> result = new ResultMessage<User>(200, "Change password success!", user);
        return Response.status(200).entity(result).build();
    }
}
