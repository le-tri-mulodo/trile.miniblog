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
import com.mulodo.miniblog.pojo.Comment;
import com.mulodo.miniblog.service.CommentService;
import com.mulodo.miniblog.service.TokenService;

import exception.NotAllowException;
import exception.ResourceNotExistException;

/**
 * RESTFUL controller to controll all about COMMENT
 * 
 * @author TriLe
 */
@Controller
@Path(Contants.URL_COMMENT)
@Produces(MediaType.APPLICATION_JSON)
@ValidateRequest
public class CommentController
{
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private TokenService tokenSer;
    @Autowired
    private CommentService commentSer;

    /**
     * Add new Comment
     * 
     * @param user_id
     *            Id of user
     * @param token
     *            Token value
     * @param post_id
     *            Id of post
     * @param pcomment_id
     *            Id of parent comment
     * @param content
     *            Content of comment
     * @return Response status and content
     */
    @SuppressWarnings({ "rawtypes", "incomplete-switch" })
    @Path(Contants.URL_ADD)
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response add(
            @NotNull(message = "{user_id.NotNull}")
            @FormParam(value = "user_id")
            @Min(value = 1, message = "{user_id.Min}")
            Integer user_id,

            @NotNull(message = "{token.NotNull}")
            @Size(min = 64, max = 64, message = "{token.Size}")
            @FormParam(value = "token")
            String token,

            @NotNull(message = "{post_id.NotNull}")
            @FormParam(value = "post_id")
            @Min(value = 1, message = "{post_id.Min}")
            Integer post_id,

            @FormParam(value = "pcomment_id")
            @Min(value = 1, message = "{pcomment_id.Min}")
            Integer pcomment_id,

            @NotNull(message = "{content.NotNull}")
            @Size(min = 1, max = 256, message = "{content.Size}")
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

        // Create new comment to call service
        Comment comment = new Comment();
        comment.setContent(content);
        // Set userId
        comment.setUserId(user_id);
        // Set postId
        comment.setPostId(post_id);
        // Set parent comment id
        if (null != pcomment_id) {
            comment.setpCommentId(pcomment_id);
        }

        // Call service to insert into Db
        try {
            commentSer.add(comment);
            // Db error
        } catch (HibernateException e) {
            // Log
            logger.warn(Contants.MSG_DB_ERR, e);
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(Contants.CODE_DB_ERR, Contants.MSG_DB_ERR,
                    String.format(Contants.FOR_DB_ERR, e.getMessage()));
            return Response.status(Contants.CODE_INTERNAL_ERR).entity(dbErrMsg).build();
        } catch (ResourceNotExistException e) {
            // Log
            logger.warn(e.getMessage());

            // Response resource not exist
            ResultMessage rsneResult = null;
            // Get type
            switch (e.getResourceType()) {
            case COMMENT:
                // Log
                logger.warn("{} with id={} not exit", e.getResourceType(), pcomment_id);

                // Response comment not exist
                rsneResult = new ResultMessage(Contants.CODE_COMMET_NOT_EXIST,
                        Contants.MSG_COMMENT_NOT_EXIST, String.format(
                                Contants.FOR_COMMENT_NOT_EXIST, pcomment_id));
                break;
            case POST:
                // Log
                logger.warn("{} with id={} not exit", e.getResourceType(), post_id);

                // Response post not exist
                rsneResult = new ResultMessage(Contants.CODE_POST_NOT_EXIST,
                        Contants.MSG_POST_NOT_EXIST, String.format(Contants.FOR_POST_NOT_EXIST,
                                post_id));
                break;
            case USER:
                // Log
                logger.warn("{} with id={} not exit", e.getResourceType(), user_id);

                // Response user not exist
                rsneResult = new ResultMessage(Contants.CODE_USER_NOT_EXIST,
                        Contants.MSG_USER_NOT_EXIST, String.format(Contants.FOR_USER_NOT_EXIST,
                                user_id));
                break;
            }

            // return Response error
            return Response.status(Contants.CODE_BAD_REQUEST).entity(rsneResult).build();
        }

        // Response success
        ResultMessage<Comment> result = new ResultMessage<Comment>(Contants.CODE_CREATED,
                Contants.MSG_CREATE_COMMENT_SCC, comment);
        return Response.status(Contants.CODE_CREATED).entity(result).build();
    }

    /**
     * Edit content of comment
     * 
     * @param user_id
     *            Id of user
     * @param token
     *            Token value
     * @param content
     *            Content of comment
     * @return Response status and content
     */
    @SuppressWarnings({ "rawtypes", "incomplete-switch" })
    @Path(Contants.URL_UPDATE)
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response update(
            @NotNull(message = "{user_id.NotNull}")
            @FormParam(value = "user_id")
            @Min(value = 1, message = "{user_id.Min}")
            Integer user_id,

            @NotNull(message = "{token.NotNull}")
            @Size(min = 64, max = 64, message = "{token.Size}")
            @FormParam(value = "token")
            String token,

            @NotNull(message = "{comment_id.NotNull}")
            @FormParam(value = "comment_id")
            @Min(value = 1, message = "{comment_id.Min}")
            Integer comment_id,

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

        // Create new comment to call service
        Comment comment = new Comment();
        // Set content
        comment.setContent(content);
        // Set comment id
        comment.setId(comment_id);
        // Set user id
        comment.setUserId(user_id);

        // Call service to insert into Db
        try {
            comment = commentSer.update(comment);
        } catch (HibernateException e) {
            // Db error
            // Log
            logger.warn(Contants.MSG_DB_ERR, e);
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(Contants.CODE_DB_ERR, Contants.MSG_DB_ERR,
                    String.format(Contants.FOR_DB_ERR, e.getMessage()));
            return Response.status(Contants.CODE_INTERNAL_ERR).entity(dbErrMsg).build();
        } catch (ResourceNotExistException e) {
            // Response resource not exist
            ResultMessage rsneResult = null;
            // Get type
            switch (e.getResourceType()) {
            case COMMENT:
                // Log
                logger.warn("{} with id={} not exit", e.getResourceType(), comment_id);

                // Response comment not exist
                rsneResult = new ResultMessage(Contants.CODE_COMMET_NOT_EXIST,
                        Contants.MSG_COMMENT_NOT_EXIST, String.format(
                                Contants.FOR_COMMENT_NOT_EXIST, comment_id));
                break;
            }
            // return Response error
            return Response.status(Contants.CODE_BAD_REQUEST).entity(rsneResult).build();
        } catch (NotAllowException e) {
            // log
            logger.warn("User with id={} is not owner of comment with id={}", user_id, comment_id);

            // Response user can not allow to access this comment
            ResultMessage forbiddenMsg = new ResultMessage(Contants.CODE_FORBIDDEN,
                    Contants.MSG_FORBIDDEN, String.format(Contants.FOR_FORBIDDEN_COMMENT, user_id,
                            comment_id));
            return Response.status(Contants.CODE_FORBIDDEN).entity(forbiddenMsg).build();
        }

        // Response success
        ResultMessage<Comment> result = new ResultMessage<Comment>(Contants.CODE_OK,
                Contants.MSG_UPDATE_COMMENT_SCC, comment);
        return Response.status(Contants.CODE_OK).entity(result).build();
    }

    @SuppressWarnings({ "rawtypes", "incomplete-switch" })
    @Path(Contants.URL_DELETE)
    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response delete(
            @NotNull(message = "{user_id.NotNull}")
            @HeaderParam(value = "user_id")
            @Min(value = 1)
            Integer user_id,

            @NotNull(message = "{token.NotNull}")
            @Size(min = 64, max = 64, message = "{token.Size}")
            @HeaderParam(value = "token")
            String token,

            @NotNull(message = "{comment_id.NotNull}")
            @HeaderParam(value = "comment_id") @Min(value = 1)
            Integer comment_id)
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
        Comment comment = new Comment();
        // Set postId
        comment.setId(comment_id);
        // Set userId
        comment.setUserId(user_id);

        // Call service to delete from Db
        try {
            commentSer.delete(comment);
        } catch (HibernateException e) {
            // Db error
            // Log
            logger.warn(Contants.MSG_DB_ERR, e);
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(Contants.CODE_DB_ERR, Contants.MSG_DB_ERR,
                    String.format(Contants.FOR_DB_ERR, e.getMessage()));
            return Response.status(Contants.CODE_INTERNAL_ERR).entity(dbErrMsg).build();
        } catch (ResourceNotExistException e) {
            // Response resource not exist
            ResultMessage rsneResult = null;
            // Get type
            switch (e.getResourceType()) {
            case COMMENT:
                // Log
                logger.warn("{} with id={} not exit", e.getResourceType(), comment_id);

                // Response comment not exist
                rsneResult = new ResultMessage(Contants.CODE_COMMET_NOT_EXIST,
                        Contants.MSG_COMMENT_NOT_EXIST, String.format(
                                Contants.FOR_COMMENT_NOT_EXIST, comment_id));
                break;
            }
            // return Response error
            return Response.status(Contants.CODE_BAD_REQUEST).entity(rsneResult).build();
        } catch (NotAllowException e) {
            // log
            logger.warn("User with id={} is not owner of comment with id={}", user_id, comment_id);

            // Response user can not allow to access this comment
            ResultMessage forbiddenMsg = new ResultMessage(Contants.CODE_FORBIDDEN,
                    Contants.MSG_FORBIDDEN, String.format(Contants.FOR_FORBIDDEN_COMMENT, user_id,
                            comment_id));
            return Response.status(Contants.CODE_FORBIDDEN).entity(forbiddenMsg).build();
        }

        // Response success
        ResultMessage result = new ResultMessage(Contants.CODE_OK, Contants.MSG_DELETE_COMMENT_SCC);
        return Response.status(Contants.CODE_OK).entity(result).build();
    }

    /**
     * List of all comments of post in Db and order by create time ascending
     * @param postId Id of post
     * @return Response status and content
     */
    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_GET_BY_POST)
    @GET
    public Response getByPostId(@PathParam("post_id") int postId)
    {
        List<Comment> comments = null;
        // Call service to get all comment of post from Db
        try {
            comments = commentSer.getByPostId(postId);
        } catch (HibernateException e) {
            // Log
            logger.warn(Contants.MSG_DB_ERR, e);
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(Contants.CODE_DB_ERR, Contants.MSG_DB_ERR,
                    String.format(Contants.FOR_DB_ERR, e.getMessage()));
            return Response.status(Contants.CODE_INTERNAL_ERR).entity(dbErrMsg).build();
        }
        // Response success
        ResultMessage<List<Comment>> result = new ResultMessage<List<Comment>>(Contants.CODE_OK,
                String.format(Contants.FOR_GET_ALL_COMMENT_SCC, comments.size()), comments);
        return Response.status(Contants.CODE_OK).entity(result).build();
    }

    /**
     * List of all comments of user in Db and order by create time ascending
     * @param postId Id of post
     * @return Response status and content
     */
    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_GET_BY_USER)
    @GET
    public Response getByUserId(@PathParam("user_id") int userId)
    {
        List<Comment> comments = null;
        // Call service to get all comment of post from Db
        try {
            comments = commentSer.getByUserId(userId);
        } catch (HibernateException e) {
            // Log
            logger.warn(Contants.MSG_DB_ERR, e);
            // Response error
            ResultMessage dbErrMsg = new ResultMessage(Contants.CODE_DB_ERR, Contants.MSG_DB_ERR,
                    String.format(Contants.FOR_DB_ERR, e.getMessage()));
            return Response.status(Contants.CODE_INTERNAL_ERR).entity(dbErrMsg).build();
        }
        // Response success
        ResultMessage<List<Comment>> result = new ResultMessage<List<Comment>>(Contants.CODE_OK,
                String.format(Contants.FOR_GET_ALL_COMMENT_SCC, comments.size()), comments);
        return Response.status(Contants.CODE_OK).entity(result).build();
    }
}
