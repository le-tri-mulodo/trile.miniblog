/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import java.sql.Timestamp;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mulodo.miniblog.common.Contants;
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

    private int userCounter = 9993;

    ResteasyClient client = new ResteasyClientBuilder().build();
    String ROOT_URL = "http://localhost:8080/miniblog.api";
    String POST_URL = ROOT_URL + Contants.URL_POST;

    private Post dummyPost = null;
    private User dummyUser = null;

    private void createDummyPost(boolean active)
    {
        // Create user
        dummyUser = new User();
        dummyUser.setUserName("thanhtri" + userCounter++);
        dummyUser.setFirstName("asxzdas");
        dummyUser.setLastName("ccrfzxc");
        dummyUser.setPassHash("password");
        userSer.add(dummyUser);

        dummyPost = new Post();

        dummyPost.setTitle("title");
        dummyPost.setDescription("description");
        dummyPost.setContent("content");
        if (active) {
            dummyPost.setPublicTime(new Timestamp(System.currentTimeMillis()));
        }

        dummyPost.setUser(dummyUser);

        dummyPost = postSer.add(dummyPost);

        Comment comment = new Comment();

        comment.setUser(dummyUser);
        comment.setPost(dummyPost);
        comment.setContent("hello");

        commentSer.add(comment);
    }

    // Normal case
    @Test
    public void testAddPost()
    {
        createDummyPost(true);
    }

}
