/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
import com.mulodo.miniblog.pojo.Comment;
import com.mulodo.miniblog.service.CommentService;
import com.mulodo.miniblog.service.TokenService;

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
    @SuppressWarnings("rawtypes")
    @Path(Contants.URL_ADD)
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response add(
            @NotNull(message = "{user_id.NotNull}")
            @FormParam(value = "user_id")
            @Min(value = 1)
            Integer user_id,

            @NotNull(message = "{token.NotNull}")
            @Size(min = 64, max = 64, message = "{token.Size}")
            @FormParam(value = "token")
            String token,

            @NotNull(message = "{post_id.NotNull}")
            @FormParam(value = "post_id")
            @Min(value = 1)
            Integer post_id,

            @FormParam(value = "pcomment_id")
            @Min(value = 1)
            Integer pcomment_id,


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
        comment.setContent(content);
        // Set userId
        comment.setUserId(user_id);
        // Set postId
        comment.setPostId(post_id);
        // Set parent comment id
        if (null != pcomment_id) {
            comment.setCommentId(pcomment_id);
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
            // Response resource not exist
            ResultMessage rsneResult = null;
            // Get type
            switch (e.getResourceType()) {
            case COMMENT:
                rsneResult = new ResultMessage(Contants.CODE_COMMET_NOT_EXIST,
                        Contants.MSG_COMMENT_NOT_EXIST, String.format(
                                Contants.FOR_COMMENT_NOT_EXIST, post_id));
                break;
            case POST:
                rsneResult = new ResultMessage(Contants.CODE_POST_NOT_EXIST,
                        Contants.MSG_POST_NOT_EXIST, String.format(Contants.FOR_POST_NOT_EXIST,
                                post_id));
                break;
            case USER:
                rsneResult = new ResultMessage(Contants.CODE_USER_NOT_EXIST,
                        Contants.MSG_USER_NOT_EXIST, String.format(Contants.FOR_USER_NOT_EXIST,
                                user_id));
                break;
            }
            return Response.status(Contants.CODE_BAD_REQUEST).entity(rsneResult).build();
        }

        // Response success
        ResultMessage<Comment> result = new ResultMessage<Comment>(Contants.CODE_CREATED,
                Contants.MSG_CREATE_COMMET_SCC, comment);
        return Response.status(Contants.CODE_CREATED).entity(result).build();
    }
}
