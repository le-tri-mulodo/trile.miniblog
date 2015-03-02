/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    String COMMENT_URL = ROOT_URL + Contants.URL_COMMENT;

    private Post dummyPost = null;
    private User dummyUser = null;
    private Comment dummyComment = null;

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
        dummyComment = new Comment();

        dummyComment.setUser(dummyUser);
        // set user id
        dummyComment.setUserId(dummyUser.getId());

        dummyComment.setPost(dummyPost);
        // set post id
        dummyComment.setPostId(dummyPost.getId());
        // set content
        dummyComment.setContent("hello");
        // add new comment
        commentSer.add(dummyComment);
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

        ResteasyWebTarget target = client.target(COMMENT_URL);

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
        // Check result
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
        comment.setCommentId(dummyComment.getId());

        ResteasyWebTarget target = client.target(COMMENT_URL);

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
        // Check result
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
        comment.setCommentId(dummyComment.getId());

        ResteasyWebTarget target = client.target(COMMENT_URL);

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
        comment.setCommentId(dummyComment.getId());

        ResteasyWebTarget target = client.target(COMMENT_URL);

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
        comment.setCommentId(dummyComment.getId());

        ResteasyWebTarget target = client.target(COMMENT_URL);

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
        comment.setCommentId(dummyComment.getId());

        ResteasyWebTarget target = client.target(COMMENT_URL);

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

        response.close();

        // Check status
        assertEquals(Contants.CODE_BAD_REQUEST, response.getStatus());
    }

    // Normal case
    @Test
    public void testEditComment()
    {
        createDummyData(true);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Form form = new Form();
        // set user id and token
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", dummyUser.getToken());
        // set comment id
        form.param("comment_id", Integer.toString(dummyComment.getId()));
        // set new content
        dummyComment.setContent("new content");
        form.param("content", dummyComment.getContent());

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Comment> result = response
                .readEntity(new GenericType<ResultMessage<Comment>>() {
                });
        response.close();

        // Check status
        assertEquals(Contants.CODE_OK, response.getStatus());
        // Check result
        assertEquals(dummyComment, result.getData());
        // check edittime not null
        assertNotNull(result.getData().getEditTime());
    }

    // miss user id
    @Test
    public void testEditComment2()
    {
        createDummyData(true);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Form form = new Form();
        // set user id and token
        // form.param("user_id", Integer.toString(dummyComment.getUserId()));
        form.param("token", dummyUser.getToken());
        // set comment id
        form.param("comment_id", Integer.toString(dummyComment.getId()));
        // set new content
        dummyComment.setContent("new content");
        form.param("content", dummyComment.getContent());

        Response response = target.request().put(Entity.form(form));

        response.close();

        // Check status
        assertEquals(Contants.CODE_BAD_REQUEST, response.getStatus());
        // Check result
    }

    // miss token
    @Test
    public void testEditComment3()
    {
        createDummyData(true);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Form form = new Form();
        // set user id and token
        form.param("user_id", Integer.toString(dummyUser.getId()));
        // form.param("token", dummyUser.getToken());
        // set comment id
        form.param("comment_id", Integer.toString(dummyComment.getId()));
        // reverse content to create new content
        form.param("content", StringUtils.reverse(dummyComment.getContent()));

        Response response = target.request().put(Entity.form(form));

        response.close();

        // Check status
        assertEquals(Contants.CODE_BAD_REQUEST, response.getStatus());
    }

    // miss content
    @Test
    public void testEditComment4()
    {
        createDummyData(true);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Form form = new Form();
        // set user id and token
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", dummyUser.getToken());
        // set comment id
        form.param("comment_id", Integer.toString(dummyComment.getId()));
        // reverse content to create new content
        // form.param("content",
        // StringUtils.reverse(dummyComment.getContent()));

        Response response = target.request().put(Entity.form(form));

        response.close();

        // Check status
        assertEquals(Contants.CODE_BAD_REQUEST, response.getStatus());
    }

    // miss comment id
    @Test
    public void testEditComment5()
    {
        createDummyData(true);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Form form = new Form();
        // set user id and token
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", dummyUser.getToken());
        // set comment id
        // form.param("comment_id", Integer.toString(dummyComment.getId()));
        // reverse content to create new content
        // form.param("content",
        // StringUtils.reverse(dummyComment.getContent()));

        Response response = target.request().put(Entity.form(form));

        response.close();

        // Check status
        assertEquals(Contants.CODE_BAD_REQUEST, response.getStatus());
    }

    // token invalid
    @Test
    public void testEditComment6()
    {
        createDummyData(true);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Form form = new Form();
        // set user id and token
        form.param("user_id", Integer.toString(dummyUser.getId()));
        // reverse token to create invalid token
        form.param("token", StringUtils.reverse(dummyUser.getToken()));
        // set comment id
        form.param("comment_id", Integer.toString(dummyComment.getId()));
        // reverse content to create new content
        form.param("content", StringUtils.reverse(dummyComment.getContent()));

        Response response = target.request().put(Entity.form(form));

        response.close();

        // Check status
        assertEquals(Contants.CODE_UNAUTHORIZED, response.getStatus());
    }

    // not allow
    @Test
    public void testEditComment7()
    {
        createDummyData(true);
        // backup dummyUser and create new user to test check owner
        User bkUser = dummyUser;
        createDummyData(true);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Form form = new Form();
        // set user id and token
        form.param("user_id", Integer.toString(bkUser.getId()));
        form.param("token", bkUser.getToken());
        // set comment id
        form.param("comment_id", Integer.toString(dummyComment.getId()));
        // reverse content to create new content
        form.param("content", StringUtils.reverse(dummyComment.getContent()));

        Response response = target.request().put(Entity.form(form));

        response.close();

        // Check status
        assertEquals(Contants.CODE_FORBIDDEN, response.getStatus());
    }

    // Normal case
    @Test
    public void testDeleteComment()
    {
        createDummyData(false);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Response response = target.request()
        // user_id
                .header("user_id", dummyUser.getId())
                // token
                .header("token", dummyUser.getToken())
                // comment id
                .header("comment_id", dummyComment.getId()).delete();

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(Contants.CODE_OK, response.getStatus());
    }

    // Miss user id
    @Test
    public void testDeleteComment2()
    {
        createDummyData(false);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Response response = target.request()
        // user_id
        // .header("user_id", dummyUser.getId())
        // token
                .header("token", dummyUser.getToken())
                // comment id
                .header("comment_id", dummyComment.getId()).delete();

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(Contants.CODE_BAD_REQUEST, response.getStatus());
    }

    // Miss token
    @Test
    public void testDeleteComment3()
    {
        createDummyData(false);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Response response = target.request()
        // user_id
                .header("user_id", dummyUser.getId())
                // token
                // .header("token", dummyUser.getToken())
                // comment id
                .header("comment_id", dummyComment.getId()).delete();

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(Contants.CODE_BAD_REQUEST, response.getStatus());
    }

    // Miss comment id
    @Test
    public void testDeleteComment4()
    {
        createDummyData(false);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Response response = target.request()
        // user_id
                .header("user_id", dummyUser.getId())
                // token
                .header("token", dummyUser.getToken())
                // comment id
                // .header("comment_id", dummyComment.getId())
                .delete();

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(Contants.CODE_BAD_REQUEST, response.getStatus());
    }

    // token invalid
    @Test
    public void testDeleteComment5()
    {
        createDummyData(false);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Response response = target.request()
        // user_id
                .header("user_id", dummyUser.getId())
                // reverse current token to create invalid token
                .header("token", StringUtils.reverse(dummyUser.getToken()))
                // comment id
                .header("comment_id", dummyComment.getId()).delete();

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(Contants.CODE_UNAUTHORIZED, response.getStatus());
    }

    // not allow
    @Test
    public void testDeleteComment6()
    {
        createDummyData(false);
        // backup dummyUser and create new user to test check owner
        User bkUser = dummyUser;
        createDummyData(true);

        ResteasyWebTarget target = client.target(COMMENT_URL);

        Response response = target.request()
        // user valid user_id and token but user not owner of comment
                .header("user_id", bkUser.getId()).header("token", bkUser.getToken())
                // comment id
                .header("comment_id", dummyComment.getId()).delete();

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(Contants.CODE_FORBIDDEN, response.getStatus());
    }
}
