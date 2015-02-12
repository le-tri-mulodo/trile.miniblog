/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mulodo.miniblog.common.Contants;
import com.mulodo.miniblog.message.ResultMessage;
import com.mulodo.miniblog.pojo.Post;
import com.mulodo.miniblog.pojo.Token;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.PostService;
import com.mulodo.miniblog.service.TokenService;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class PostControllerTest {

    @Autowired
    private UserService userSer;
    @Autowired
    private TokenService tokenSer;
    @Autowired
    private PostService postSer;

    ResteasyClient client = new ResteasyClientBuilder().build();
    String ROOT_URL = "http://localhost:8080/miniblog.api";
    String POST_URL = ROOT_URL + Contants.URL_POST;

    @Before
    public void prepareData() {
        // Delete all data
        postSer.deleteAll();
        tokenSer.deleteAll();
        userSer.deleteAll();
    }

    private Token createDummyData() {
        // Create user
        User user = new User();
        user = new User();
        user.setUserName("thanhtri");
        user.setFirstName("asxzdas");
        user.setLastName("ccrfzxc");
        user.setPassHash("password");
        userSer.add(user);
        // Create token
        return tokenSer.createNewToken(user);
    }

    // Normal case
    @Test
    public void testAddPost() {
        Token token = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(token.getUserid());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(token.getUserid()));
        form.param("token", token.getValue());
        form.param("title", post.getTitle());
        form.param("description", post.getDescription());
        form.param("content", post.getContent());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(201, response.getStatus());
        // Check token not null
        assertEquals(post, result.getData());
    }

    // Miss user_id
    @Test
    public void testAddPost1() {
        Token token = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(token.getUserid());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("token", token.getValue());
        form.param("title", post.getTitle());
        form.param("description", post.getDescription());
        form.param("content", post.getContent());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss token
    @Test
    public void testAddPost2() {
        Token token = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(token.getUserid());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(token.getUserid()));
        form.param("title", post.getTitle());
        form.param("description", post.getDescription());
        form.param("content", post.getContent());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss title
    @Test
    public void testAddPost3() {
        Token token = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(token.getUserid());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(token.getUserid()));
        form.param("token", token.getValue());
        form.param("description", post.getDescription());
        form.param("content", post.getContent());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss description
    @Test
    public void testAddPost4() {
        Token token = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(token.getUserid());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(token.getUserid()));
        form.param("token", token.getValue());
        form.param("title", post.getTitle());
        form.param("content", post.getContent());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss content
    @Test
    public void testAddPost5() {
        Token token = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(token.getUserid());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(token.getUserid()));
        form.param("token", token.getValue());
        form.param("title", post.getTitle());
        form.param("description", post.getDescription());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Token invalid
    @Test
    public void testAddPost6() {
        Token token = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(token.getUserid());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(token.getUserid()));
        // Reverse token to create invalid token
        form.param("token", StringUtils.reverse(token.getValue()));
        form.param("title", post.getTitle());
        form.param("description", post.getDescription());
        form.param("content", post.getContent());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(401, response.getStatus());
    }
}
