/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

}
