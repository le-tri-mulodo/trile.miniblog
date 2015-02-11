/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import com.mulodo.miniblog.pojo.Token;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.TokenService;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 */

@Controller
@Path(Contants.URL_TOKEN)
@Produces(MediaType.APPLICATION_JSON)
@ValidateRequest
public class TokenController {
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private UserService userSer;
    @Autowired
    private TokenService tokenSer;

    /**
     * Create new token and response to user if input username and password valid
     * @param username
     * @param password
     * @return Response to client
     */
    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_LOGIN)
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response userLogin(
            @NotNull(message = "{username.NotNull}")
            @Pattern(regexp = Contants.WORDS_VALID_REGEX, message = "{username.Invalid}")
            @Size(min = 4, max = 64, message = "{username.Size}") @FormParam("username")
            String username,

            @NotNull(message = "{password.NotNull}")
            @Size(min = 4, max = 999, message = "{password.Size}")
            @FormParam("password")
            String password) {

        User userInfo = userSer.checkPasswordGetUserInfo(username, password);
        if (null == userInfo) {
            // Response username or password invalid
            ResultMessage errorMsg = new ResultMessage(1002,
                    "Login with username or password invalid", "User id or password invalid");
            return Response.status(401).entity(errorMsg).build();
        }

        Token token = null;
        try {
            token = tokenSer.login(userInfo);
        } catch (HibernateException e) {
            // Response db error
            ResultMessage dbErrMsg = new ResultMessage(9001, "Database access error",
                    String.format("Database error: %s", e.getMessage()));
            return Response.status(500).entity(dbErrMsg).build();
        }

        // Response success
        ResultMessage<Token> successMsg = new ResultMessage<Token>(200, "Login success!", token);
        return Response.status(200).entity(successMsg).build();
    }

    /**
     * Delete valid token in Db
     * 
     * @param token
     * @return Response to client
     */
    @Path(Contants.URL_LOGOUT)
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response userLogout(
            @NotNull(message = "{token.NotNull}")
            @Size(min = 64, max = 64, message = "{token.Size}")
            @FormParam(value = "token")
            String token) {

        boolean deleteStatus;
        try {
            deleteStatus = tokenSer.logout(token);
        } catch (HibernateException e) {
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(9001, "Database access error",
                    String.format("Database error: %s", e.getMessage()));
            return Response.status(500).entity(dbErrMsg).build();
        }

        if (deleteStatus) {
            // Response success
            ResultMessage successMsg = new ResultMessage(200, "Logout success!");
            return Response.status(200).entity(successMsg).build();
        }

        // Response token invalid or expired
        ResultMessage unauthorizedMsg = new ResultMessage(1001,
                "Token in request invaild or expired", String.format("Token [%s] invaild or expired",
                        token));
        return Response.status(401).entity(unauthorizedMsg).build();
    }

}
