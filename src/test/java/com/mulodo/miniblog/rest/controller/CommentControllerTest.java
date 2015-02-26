/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mulodo.miniblog.common.Contants;
import com.mulodo.miniblog.message.ResultMessage;
import com.mulodo.miniblog.pojo.Comment;
import com.mulodo.miniblog.pojo.Post;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.CommentService;
import com.mulodo.miniblog.service.PostService;
import com.mulodo.miniblog.service.TokenService;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class CommentControllerTest
{

    @Autowired
    private UserService userSer;
    @Autowired
    private TokenService tokenSer;
    @Autowired
    private PostService postSer;
    @Autowired
    private CommentService commentSer;

    ResteasyClient client = new ResteasyClientBuilder().build();
    String ROOT_URL = "http://localhost:8080/miniblog.api";
    String POST_URL = ROOT_URL + Contants.URL_COMMENT;

    private Post dummyPost = null;
    private User dummyUser = null;
    private Comment dummycomment = null;

    // Prepair data to test
    // Create user, post, and comment
    private void createDummyData(boolean active)
    {
        // Create user
        dummyUser = new User();
        dummyUser.setUserName("thanhtri" + System.currentTimeMillis());
        dummyUser.setFirstName("asxzdas");
        dummyUser.setLastName("ccrfzxc");
        dummyUser.setPassHash("password");
        // add user
        userSer.add(dummyUser);

        dummyPost = new Post();

        dummyPost.setTitle("title");
        dummyPost.setDescription("description");
        dummyPost.setContent("content");
        // set post is public post or not
        if (active) {
            dummyPost.setPublicTime(new Timestamp(System.currentTimeMillis()));
        }

        dummyPost.setUser(dummyUser);
        // add post
        dummyPost = postSer.add(dummyPost);

        // parent comment
        dummycomment = new Comment();

        dummycomment.setUser(dummyUser);
        dummycomment.setPost(dummyPost);
        dummycomment.setContent("hello");
        // add new comment
        commentSer.add(dummycomment);
    }

    // Normal case (not sub-comment)
    @Test
    public void testAddComment()
    {
        createDummyData(true);

        // Create expected comment
        Comment comment = new Comment();
        // User ID
        comment.setUserId(dummyUser.getId());
        // set content
        comment.setContent("content");
        // set post id (use in assert)
        comment.setPostId(dummyPost.getId());

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        // set user id and token
        form.param("user_id", Integer.toString(comment.getUserId()));
        form.param("token", dummyUser.getToken());
        // set content of comment
        form.param("content", comment.getContent());
        // set post id
        form.param("post_id", Integer.toString(comment.getPostId()));

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Comment> result = response
                .readEntity(new GenericType<ResultMessage<Comment>>() {
                });
        response.close();

        // Check status
        assertEquals(Contants.CODE_CREATED, response.getStatus());
        // Check token not null
        assertEquals(comment, result.getData());
    }

    // Normal case (sub-comment)
    @Test
    public void testAddComment2()
    {
        createDummyData(true);

        // Create expected comment
        Comment comment = new Comment();
        // User ID
        comment.setUserId(dummyUser.getId());
        // set content
        comment.setContent("content");
        // set post id (use in assert)
        comment.setPostId(dummyPost.getId());
        // set parent-comment
        comment.setCommentId(dummycomment.getId());

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        // set user id and token
        form.param("user_id", Integer.toString(comment.getUserId()));
        form.param("token", dummyUser.getToken());
        // set content of comment
        form.param("content", comment.getContent());
        // set post id
        form.param("post_id", Integer.toString(comment.getPostId()));
        // set parent-comment
        form.param("pcomment_id", comment.getCommentId().toString());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Comment> result = response
                .readEntity(new GenericType<ResultMessage<Comment>>() {
                });
        response.close();

        // Check status
        assertEquals(Contants.CODE_CREATED, response.getStatus());
        // Check token not null
        assertEquals(comment, result.getData());
    }

    // Miss user id
    @Test
    public void testAddComment3()
    {
        createDummyData(true);

        // Create expected comment
        Comment comment = new Comment();
        // User ID
        comment.setUserId(dummyUser.getId());
        // set content
        comment.setContent("content");
        // set post id (use in assert)
        comment.setPostId(dummyPost.getId());
        // set parent-comment
        comment.setCommentId(dummycomment.getId());

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        // set user id and token
        // form.param("user_id", Integer.toString(comment.getUserId()));
        form.param("token", dummyUser.getToken());
        // set content of comment
        form.param("content", comment.getContent());
        // set post id
        form.param("post_id", Integer.toString(comment.getPostId()));
        // set parent-comment
        form.param("pcomment_id", comment.getCommentId().toString());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Comment> result = response
                .readEntity(new GenericType<ResultMessage<Comment>>() {
                });
        response.close();

        // Check status
        assertEquals(Contants.CODE_BAD_REQUEST, response.getStatus());
    }

    // token invalid
    @Test
    public void testAddComment4()
    {
        createDummyData(true);

        // Create expected comment
        Comment comment = new Comment();
        // User ID
        comment.setUserId(dummyUser.getId());
        // set content
        comment.setContent("content");
        // set post id (use in assert)
        comment.setPostId(dummyPost.getId());
        // set parent-comment
        comment.setCommentId(dummycomment.getId());

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        // set user id and token
        form.param("user_id", Integer.toString(comment.getUserId()));
        // reverse string to create invalid token
        form.param("token", StringUtils.reverse(dummyUser.getToken()));
        // set content of comment
        form.param("content", comment.getContent());
        // set post id
        form.param("post_id", Integer.toString(comment.getPostId()));
        // set parent-comment
        form.param("pcomment_id", comment.getCommentId().toString());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Comment> result = response
                .readEntity(new GenericType<ResultMessage<Comment>>() {
                });
        response.close();

        // Check status
        assertEquals(Contants.CODE_UNAUTHORIZED, response.getStatus());
    }

    // miss content
    @Test
    public void testAddComment5()
    {
        createDummyData(true);

        // Create expected comment
        Comment comment = new Comment();
        // User ID
        comment.setUserId(dummyUser.getId());
        // set content
        comment.setContent("content");
        // set post id (use in assert)
        comment.setPostId(dummyPost.getId());
        // set parent-comment
        comment.setCommentId(dummycomment.getId());

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        // set user id and token
        form.param("user_id", Integer.toString(comment.getUserId()));
        // reverse string to create invalid token
        form.param("token", StringUtils.reverse(dummyUser.getToken()));
        // set content of comment
        // form.param("content", comment.getContent());
        // set post id
        form.param("post_id", Integer.toString(comment.getPostId()));
        // set parent-comment
        form.param("pcomment_id", comment.getCommentId().toString());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Comment> result = response
                .readEntity(new GenericType<ResultMessage<Comment>>() {
                });
        response.close();

        // Check status
        assertEquals(Contants.CODE_BAD_REQUEST, response.getStatus());
    }

    // miss post id
    @Test
    public void testAddComment6()
    {
        createDummyData(true);

        // Create expected comment
        Comment comment = new Comment();
        // User ID
        comment.setUserId(dummyUser.getId());
        // set content
        comment.setContent("content");
        // set post id (use in assert)
        comment.setPostId(dummyPost.getId());
        // set parent-comment
        comment.setCommentId(dummycomment.getId());

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        // set user id and token
        form.param("user_id", Integer.toString(comment.getUserId()));
        // reverse string to create invalid token
        form.param("token", StringUtils.reverse(dummyUser.getToken()));
        // set content of comment
        // form.param("content", comment.getContent());
        // set post id
        form.param("post_id", Integer.toString(comment.getPostId()));
        // set parent-comment
        form.param("pcomment_id", comment.getCommentId().toString());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Comment> result = response
                .readEntity(new GenericType<ResultMessage<Comment>>() {
                });
        response.close();

        // Check status
        assertEquals(Contants.CODE_BAD_REQUEST, response.getStatus());
    }
}
