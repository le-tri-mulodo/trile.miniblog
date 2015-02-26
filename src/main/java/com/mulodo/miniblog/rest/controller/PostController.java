/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
public class PostController
{
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private TokenService tokenSer;
    @Autowired
    private PostService postSer;

    @SuppressWarnings("rawtypes")
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
            String content)
    {

        // Check token
        if (!tokenSer.checkToken(user_id, token)) {
            // Log
            logger.warn("Token {} invaild or expired", token);
            // Unauthorized
            ResultMessage unauthorizedMsg = new ResultMessage(Contants.CODE_TOKEN_ERR,
                    Contants.MSG_TOKEN_ERR, String.format(Contants.FOR_TOKEN_ERR, token));
            return Response.status(Contants.CODE_UNAUTHORIZED).entity(unauthorizedMsg).build();
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
            // Log
            logger.warn(Contants.MSG_DB_ERR, e);
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(Contants.CODE_DB_ERR, Contants.MSG_DB_ERR,
                    String.format(Contants.FOR_DB_ERR, e.getMessage()));
            return Response.status(Contants.CODE_INTERNAL_ERR).entity(dbErrMsg).build();
        }

        // Response success
        ResultMessage<Post> result = new ResultMessage<Post>(Contants.CODE_CREATED,
                Contants.MSG_CREATE_POST_SCC, post);
        return Response.status(Contants.CODE_CREATED).entity(result).build();
    }

    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_UPDATE)
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response update(
            @NotNull(message = "{user_id.NotNull}")
            @FormParam(value = "user_id")
            @Min(value = 0)
            Integer user_id,

            @NotNull(message = "{token.NotNull}")
            @Size(min = 64, max = 64, message = "{token.Size}")
            @FormParam(value = "token")
            String token,

            @NotNull(message = "{post_id.NotNull}")
            @FormParam(value = "post_id")
            @Min(value = 0)
            Integer post_id,

            @Size(min = 1, max = 128, message = "{title.Size}")
            @FormParam(value = "title")
            String title,

            @Size(min = 1, max = 128, message = "{description.Size}")
            @FormParam(value = "description")
            String description,

            @Size(min = 1, max = 8192, message = "{content.Size}")
            @FormParam(value = "content")
            String content)
    {

        // Check have any field change?
        if (null == title && null == description && null == content) {
            // Log
            logger.warn(Contants.MSG_MISS_ALL_FIELDS);
            // Miss all fields
            ResultMessage missAllFieldsMsg = new ResultMessage(Contants.CODE_INPUT_ERR,
                    Contants.MSG_MISS_ALL_FIELDS, Contants.MSG_MISS_ALL_FIELDS_DTL);
            return Response.status(Contants.CODE_BAD_REQUEST).entity(missAllFieldsMsg).build();
        }

        // Check token
        if (!tokenSer.checkToken(user_id, token)) {
            // Log
            logger.warn("Token {} invaild or expired", token);
            // Unauthorized
            ResultMessage unauthorizedMsg = new ResultMessage(Contants.CODE_TOKEN_ERR,
                    Contants.MSG_TOKEN_ERR, String.format(Contants.FOR_TOKEN_ERR, token));
            return Response.status(Contants.CODE_UNAUTHORIZED).entity(unauthorizedMsg).build();
        }

        // Check owner
        if (!postSer.checkOwner(post_id, user_id)) {
            logger.warn("Token in request invaild or expired");
            // Response username or password invalid
            ResultMessage forbiddenMsg = new ResultMessage(Contants.CODE_FORBIDDEN,
                    Contants.MSG_FORBIDDEN, String.format(Contants.FOR_FORBIDDEN_POST, user_id,
                            post_id));
            return Response.status(Contants.CODE_FORBIDDEN).entity(forbiddenMsg).build();
        }

        // Create new post to call service
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setContent(content);
        // Set postId
        post.setId(post_id);

        // Call service to update into Db
        try {
            post = postSer.update(post);
        } catch (HibernateException e) {
            // Log
            logger.warn(Contants.MSG_DB_ERR, e);
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(Contants.CODE_DB_ERR, Contants.MSG_DB_ERR,
                    String.format(Contants.FOR_DB_ERR, e.getMessage()));
            return Response.status(Contants.CODE_INTERNAL_ERR).entity(dbErrMsg).build();
        }

        // Response success
        ResultMessage<Post> result = new ResultMessage<Post>(Contants.CODE_OK,
                Contants.MSG_UPDATE_POST_SCC, post);
        return Response.status(Contants.CODE_OK).entity(result).build();
    }

    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_DELETE)
    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response delete(
            @NotNull(message = "{user_id.NotNull}")
            @HeaderParam(value = "user_id")
            @Min(value = 0)
            Integer user_id,

            @NotNull(message = "{token.NotNull}")
            @Size(min = 64, max = 64, message = "{token.Size}")
            @HeaderParam(value = "token")
            String token,

            @NotNull(message = "{post_id.NotNull}")
            @HeaderParam(value = "post_id") @Min(value = 0)
            Integer post_id)
    {

        // Check token
        if (!tokenSer.checkToken(user_id, token)) {
            // Log
            logger.warn("Token {} invaild or expired", token);
            // Unauthorized
            ResultMessage unauthorizedMsg = new ResultMessage(Contants.CODE_TOKEN_ERR,
                    Contants.MSG_TOKEN_ERR, String.format(Contants.FOR_TOKEN_ERR, token));
            return Response.status(Contants.CODE_UNAUTHORIZED).entity(unauthorizedMsg).build();
        }

        // Check owner
        if (!postSer.checkOwner(post_id, user_id)) {
            logger.warn("Token in request invaild or expired");
            // Response username or password invalid
            ResultMessage forbiddenMsg = new ResultMessage(Contants.CODE_FORBIDDEN,
                    Contants.MSG_FORBIDDEN, String.format(Contants.FOR_FORBIDDEN_POST, user_id,
                            post_id));
            return Response.status(Contants.CODE_FORBIDDEN).entity(forbiddenMsg).build();
        }

        // Create new post to call service
        Post post = new Post();
        // Set postId
        post.setId(post_id);

        // Call service to delete from Db
        try {
            postSer.delete(post);
        } catch (HibernateException e) {
            // Log
            logger.warn(Contants.MSG_DB_ERR, e);
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(Contants.CODE_DB_ERR, Contants.MSG_DB_ERR,
                    String.format(Contants.FOR_DB_ERR, e.getMessage()));
            return Response.status(Contants.CODE_INTERNAL_ERR).entity(dbErrMsg).build();
        }

        // Response success
        ResultMessage result = new ResultMessage(Contants.CODE_OK, Contants.MSG_DELETE_POST_SCC);
        return Response.status(Contants.CODE_OK).entity(result).build();
    }

    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_PUBLICT)
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response activeDeactive(
            @NotNull(message = "{user_id.NotNull}")
            @FormParam(value = "user_id")
            @Min(value = 0)
            Integer user_id,

            @NotNull(message = "{token.NotNull}")
            @Size(min = 64, max = 64, message = "{token.Size}")
            @FormParam(value = "token")
            String token,

            @NotNull(message = "{post_id.NotNull}")
            @FormParam(value = "post_id")
            @Min(value = 0)
            Integer post_id,

            @NotNull(message = "{active.NotNull}")
            @FormParam(value = "active")
            Boolean active)
    {

        // Check token
        if (!tokenSer.checkToken(user_id, token)) { // Log
            logger.warn("Token {} invaild or expired", token);
            // Unauthorized
            ResultMessage unauthorizedMsg = new ResultMessage(Contants.CODE_TOKEN_ERR,
                    Contants.MSG_TOKEN_ERR, String.format(Contants.FOR_TOKEN_ERR, token));
            return Response.status(Contants.CODE_UNAUTHORIZED).entity(unauthorizedMsg).build();
        }

        // Check owner
        if (!postSer.checkOwner(post_id, user_id)) {
            logger.warn("Token in request invaild or expired");
            // Response username or password invalid
            ResultMessage forbiddenMsg = new ResultMessage(Contants.CODE_FORBIDDEN,
                    Contants.MSG_FORBIDDEN, String.format(Contants.FOR_FORBIDDEN_POST, user_id,
                            post_id));
            return Response.status(Contants.CODE_FORBIDDEN).entity(forbiddenMsg).build();
        }

        Post post = null;
        // Call service to insert into Db
        try {
            post = postSer.activeDeactive(post_id, active);
        } catch (HibernateException e) {
            // Log
            logger.warn(Contants.MSG_DB_ERR, e);
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(Contants.CODE_DB_ERR, Contants.MSG_DB_ERR,
                    String.format(Contants.FOR_DB_ERR, e.getMessage()));
            return Response.status(Contants.CODE_INTERNAL_ERR).entity(dbErrMsg).build();
        }

        // Response success
        ResultMessage<Post> result = new ResultMessage<Post>(Contants.CODE_OK,
                Contants.MSG_ACT_DEACT_SCC, post);
        return Response.status(Contants.CODE_OK).entity(result).build();
    }

    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_GET)
    @GET
    public Response allPost()
    {
        List<Post> posts = null;
        // Call service to get all public post from Db
        try {
            posts = postSer.list();
        } catch (HibernateException e) {
            // Log
            logger.warn(Contants.MSG_DB_ERR, e);
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(Contants.CODE_DB_ERR, Contants.MSG_DB_ERR,
                    String.format(Contants.FOR_DB_ERR, e.getMessage()));
            return Response.status(Contants.CODE_INTERNAL_ERR).entity(dbErrMsg).build();
        }
        // Response success
        ResultMessage<List<Post>> result = new ResultMessage<List<Post>>(Contants.CODE_OK,
                String.format(Contants.FOR_GET_ALL_POST_SCC, posts.size()), posts);
        return Response.status(Contants.CODE_OK).entity(result).build();
    }

    @Path(Contants.URL_TOP)
    @GET
    public Response topPost()
    {
        return Response.status(200).build();
    }

    @Path(Contants.URL_TOP)
    @GET
    public Response getById(@FormParam("token") String token)
    {
        return Response.status(200).build();
    }

    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_GET_BY_USER)
    @GET
    public Response getByUserId(@PathParam("user_id") int userId)
    {
        List<Post> posts = null;
        // Call service to get all public post of user from Db
        try {
            posts = postSer.getByUserId(userId);
        } catch (HibernateException e) {
            // Log
            logger.warn(Contants.MSG_DB_ERR, e);
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(Contants.CODE_DB_ERR, Contants.MSG_DB_ERR,
                    String.format(Contants.FOR_DB_ERR, e.getMessage()));
            return Response.status(Contants.CODE_INTERNAL_ERR).entity(dbErrMsg).build();
        }
        // Response success
        ResultMessage<List<Post>> result = new ResultMessage<List<Post>>(Contants.CODE_OK,
                String.format(Contants.FOR_GET_ALL_POST_SCC, posts.size()), posts);
        return Response.status(Contants.CODE_OK).entity(result).build();
    }

    @Path(Contants.URL_SEARCH)
    @GET
    public Response search(@PathParam("query") String query)
    {
        return Response.status(200).build();
    }
}
