/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.mulodo.miniblog.common.Contants;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 */

@Controller
@Path(Contants.URL_TOKEN)
@Produces(MediaType.APPLICATION_JSON)
public class TokenController {
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    UserService userSer;

    @Path(Contants.URL_LOGIN)
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response userLogin(@FormParam("username") String username,
            @FormParam("password") String password) {
        int status = 401;
        if ("tri".equals(username)) {
            status = 200;
        }
        logger.info("User: {}, pass {}", username, password);

        User user = new User();
        user.setPassHash(password);
        user.setUserName(username);
        user.setFirstName(username + password);
        user.setLastName(password + username);
        user.setJoinDate(new Date());

        userSer.add(user);

        // userSer.search(password);

        return Response.status(status).entity(user).build();
    }

    @Path(Contants.URL_LOGOUT)
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response userLogout(@FormParam("token") String token) {
        return Response.status(200).build();
    }

}
