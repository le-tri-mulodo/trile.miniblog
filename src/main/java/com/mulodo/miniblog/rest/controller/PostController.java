/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

import org.hibernate.HibernateException;
import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.mulodo.miniblog.common.Contants;
import com.mulodo.miniblog.message.ResultMessage;
import com.mulodo.miniblog.pojo.Post;
import com.mulodo.miniblog.service.PostService;
import com.mulodo.miniblog.service.TokenService;

/**
 * @author TriLe
 */
@Controller
@Path(Contants.URL_POST)
@Produces(MediaType.APPLICATION_JSON)
@ValidateRequest
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    
    @Autowired
    private TokenService tokenSer;
    @Autowired
    private PostService postSer;

    @Path(Contants.URL_ADD)
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response add(
            @NotNull(message = "{user_id.NotNull}")
            @FormParam(value = "user_id")
            @Min(value = 0)
            Integer user_id,

            @NotNull(message = "{token.NotNull}")
            @Size(min = 64, max = 64, message = "{token.Size}")
            @FormParam(value = "token")
            String token,

            @NotNull(message = "{title.NotNull}")
            @Size(min = 1, max = 128, message = "{title.Size}")
            @FormParam(value = "title")
            String title,

            @NotNull(message = "{description.NotNull}")
            @Size(min = 1, max = 128, message = "{description.Size}")
            @FormParam(value = "description")
            String description,

            @NotNull(message = "{content.NotNull}")
            @Size(min = 1, max = 8192, message = "{content.Size}")
            @FormParam(value = "content")
            String content) {

        // Check token
        if (!tokenSer.checkToken(user_id, token)) {
            // Response username or password invalid
            ResultMessage unauthorizedMsg = new ResultMessage(1001,
                    "Token in request invaild or expired", String.format(
                            "Token [%s] invaild or expired", token));
            return Response.status(401).entity(unauthorizedMsg).build();
        }

        // Create new post to call service
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setContent(content);
        // Set userId
        post.setUserId(user_id);

        // Call service to insert into Db
        try {
            postSer.add(post);
        } catch (HibernateException e) {
            // Response db error
            ResultMessage dbErrMsg = new ResultMessage(9001, "Database access error",
                    String.format("Database error: %s", e.getMessage()));
            return Response.status(500).entity(dbErrMsg).build();
        }

        // Response success
        ResultMessage<Post> result = new ResultMessage<Post>(201, "Create post success!", post);
        return Response.status(201).entity(result).build();
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
