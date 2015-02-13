/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;

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
public class PostControllerTest
{

    @Autowired
    private UserService userSer;
    @Autowired
    private TokenService tokenSer;
    @Autowired
    private PostService postSer;

    ResteasyClient client = new ResteasyClientBuilder().build();
    String ROOT_URL = "http://localhost:8080/miniblog.api";
    String POST_URL = ROOT_URL + Contants.URL_POST;

    private Token dummyToken = null;
    private Post dummyPost = null;
    private User dummyUser = null;

    @Before
    public void prepareData()
    {
        // Delete all data
        postSer.deleteAll();
        tokenSer.deleteAll();
        userSer.deleteAll();
    }

    private User createDummyData()
    {
        // Create user
        User user = new User();
        user = new User();
        user.setUserName("thanhtri");
        user.setFirstName("asxzdas");
        user.setLastName("ccrfzxc");
        user.setPassHash("password");

        return userSer.add(user);
    }

    private void createDummyPost(boolean active)
    {
        // Create user
        dummyUser = new User();
        dummyUser.setUserName("thanhtri");
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
    }

    // Normal case
    @Test
    // @Ignore
    public void testAddPost()
    {
        User user = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(user.getId());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
        form.param("token", user.getToken());
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
    // @Ignore
    public void testAddPost1()
    {
        User user = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(user.getId());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("token", user.getToken());
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
    // @Ignore
    public void testAddPost2()
    {
        User user = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(user.getId());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
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
    // @Ignore
    public void testAddPost3()
    {
        User user = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(user.getId());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
        form.param("token", user.getToken());
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
    // @Ignore
    public void testAddPost4()
    {
        User user = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(user.getId());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
        form.param("token", user.getToken());
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
    // @Ignore
    public void testAddPost5()
    {
        User user = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(user.getId());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
        form.param("token", user.getToken());
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
    // @Ignore
    public void testAddPost6()
    {
        User user = createDummyData();

        // Create new post
        Post post = new Post();
        // User ID
        post.setUserId(user.getId());
        post.setTitle("title");
        post.setDescription("description");
        post.setContent("content");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
        // Reverse token to create invalid token
        form.param("token", StringUtils.reverse(user.getToken()));
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

    // Normal case. Active, change
    @Test
    // @Ignore
    public void testActivePost()
    {
        createDummyPost(false);

        ResteasyWebTarget target = client.target(POST_URL + Contants.URL_PUBLICT);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", dummyUser.getToken());
        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("active", Boolean.toString(true));

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(200, response.getStatus());

        Post post = result.getData();
        assertNotNull(post.getPublicTime());
        // Check public time
        // Ignore public time
        post.setPublicTime(null);
        // Check
        assertEquals(dummyPost, post);
    }

    // Normal case. Active, not change
    @Test
    // @Ignore
    public void testActivePost1()
    {
        createDummyPost(true);

        ResteasyWebTarget target = client.target(POST_URL + Contants.URL_PUBLICT);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", dummyUser.getToken());
        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("active", Boolean.toString(true));

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(200, response.getStatus());

        Post post = result.getData();
        // Check
        assertEquals(dummyPost, post);
    }

    // Normal case. Deactive, change
    @Test
    // @Ignore
    public void testDeactivePost()
    {
        createDummyPost(true);

        ResteasyWebTarget target = client.target(POST_URL + Contants.URL_PUBLICT);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", dummyUser.getToken());
        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("active", Boolean.toString(false));

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(200, response.getStatus());

        Post post = result.getData();
        assertNull(post.getPublicTime());
        // Check public time
        // Ignore public time
        dummyPost.setPublicTime(null);
        // Check
        assertEquals(dummyPost, post);
    }

    // Normal case. Deactive, not change
    @Test
    // @Ignore
    public void testDeactivePost1()
    {
        createDummyPost(false);

        ResteasyWebTarget target = client.target(POST_URL + Contants.URL_PUBLICT);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", dummyUser.getToken());
        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("active", Boolean.toString(false));

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(200, response.getStatus());

        Post post = result.getData();
        // Check
        assertEquals(dummyPost, post);
    }

    // Token invalid
    @Test
    // @Ignore
    public void testActiveDeactivePost1()
    {
        createDummyPost(false);

        ResteasyWebTarget target = client.target(POST_URL + Contants.URL_PUBLICT);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", StringUtils.reverse(dummyUser.getToken()));
        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("active", Boolean.toString(false));

        Response response = target.request().put(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(401, response.getStatus());
    }

    // Miss userId
    @Test
    // @Ignore
    public void testActiveDeactivePost2()
    {
        createDummyPost(false);

        ResteasyWebTarget target = client.target(POST_URL + Contants.URL_PUBLICT);

        Form form = new Form();
        form.param("token", StringUtils.reverse(dummyUser.getToken()));
        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("active", Boolean.toString(false));

        Response response = target.request().put(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss token
    @Test
    // @Ignore
    public void testActiveDeactivePost3()
    {
        createDummyPost(false);

        ResteasyWebTarget target = client.target(POST_URL + Contants.URL_PUBLICT);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("active", Boolean.toString(false));

        Response response = target.request().put(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss postId
    @Test
    // @Ignore
    public void testActiveDeactivePost4()
    {
        createDummyPost(false);

        ResteasyWebTarget target = client.target(POST_URL + Contants.URL_PUBLICT);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", StringUtils.reverse(dummyUser.getToken()));
        form.param("active", Boolean.toString(false));

        Response response = target.request().put(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss active flag
    @Test
    // @Ignore
    public void testActiveDeactivePost5()
    {
        createDummyPost(false);

        ResteasyWebTarget target = client.target(POST_URL + Contants.URL_PUBLICT);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", StringUtils.reverse(dummyUser.getToken()));
        form.param("post_id", Integer.toString(dummyPost.getId()));

        Response response = target.request().put(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Normal case
    @Test
    public void testUpdatePost()
    {
        createDummyPost(false);

        // Change dummy post which existed in Db
        dummyPost.setTitle("title-changed");
        dummyPost.setDescription("description-changed");
        dummyPost.setContent("content-changed");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", dummyUser.getToken());
        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("title", dummyPost.getTitle());
        form.param("description", dummyPost.getDescription());
        form.param("content", dummyPost.getContent());

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(200, response.getStatus());

        Post post = result.getData();
        // Check edit time not null

        assertNotNull(post.getEditTime());
        // Ignore edit time
        post.setEditTime(null);
        // Check post
        assertEquals(dummyPost, post);
    }

    // Miss userId
    @Test
    public void testUpdatePost2()
    {
        createDummyPost(false);

        // Change dummy post which existed in Db
        dummyPost.setTitle("title-changed");
        dummyPost.setDescription("description-changed");
        dummyPost.setContent("content-changed");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("token", dummyUser.getToken());
        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("title", dummyPost.getTitle());
        form.param("description", dummyPost.getDescription());
        form.param("content", dummyPost.getContent());

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss token
    @Test
    public void testUpdatePost3()
    {
        createDummyPost(false);

        // Change dummy post which existed in Db
        dummyPost.setTitle("title-changed");
        dummyPost.setDescription("description-changed");
        dummyPost.setContent("content-changed");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("title", dummyPost.getTitle());
        form.param("description", dummyPost.getDescription());
        form.param("content", dummyPost.getContent());

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss post_id
    @Test
    public void testUpdatePost4()
    {
        createDummyPost(false);

        // Change dummy post which existed in Db
        dummyPost.setTitle("title-changed");
        dummyPost.setDescription("description-changed");
        dummyPost.setContent("content-changed");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", dummyUser.getToken());
        form.param("title", dummyPost.getTitle());
        form.param("description", dummyPost.getDescription());
        form.param("content", dummyPost.getContent());

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss title, description and content
    @Test
    public void testUpdatePost5()
    {
        createDummyPost(false);

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        form.param("token", dummyUser.getToken());
        form.param("post_id", Integer.toString(dummyPost.getId()));

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Token invalid
    @Test
    public void testUpdatePost6()
    {
        createDummyPost(false);

        // Change dummy post which existed in Db
        dummyPost.setTitle("title-changed");
        dummyPost.setDescription("description-changed");
        dummyPost.setContent("content-changed");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        form.param("user_id", Integer.toString(dummyUser.getId()));
        // Revert to create invalid token
        form.param("token", StringUtils.reverse(dummyUser.getToken()));
        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("title", dummyPost.getTitle());
        form.param("description", dummyPost.getDescription());
        form.param("content", dummyPost.getContent());

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(401, response.getStatus());
    }

    // Not accessable
    @Test
    public void testUpdatePost7()
    {
        createDummyPost(false);
        // Create other user
        User otherUser = new User();
        otherUser = new User();
        otherUser.setUserName("thanhtri2");
        otherUser.setFirstName("asxzdas");
        otherUser.setLastName("ccrfzxc");
        otherUser.setPassHash("password");

        otherUser = userSer.add(otherUser);

        // Change dummy post which existed in Db
        dummyPost.setTitle("title-changed");
        dummyPost.setDescription("description-changed");
        dummyPost.setContent("content-changed");

        ResteasyWebTarget target = client.target(POST_URL);

        Form form = new Form();
        // Other user and token
        form.param("user_id", Integer.toString(otherUser.getId()));
        form.param("token", otherUser.getToken());

        form.param("post_id", Integer.toString(dummyPost.getId()));
        form.param("title", dummyPost.getTitle());
        form.param("description", dummyPost.getDescription());
        form.param("content", dummyPost.getContent());

        Response response = target.request().put(Entity.form(form));

        ResultMessage<Post> result = response.readEntity(new GenericType<ResultMessage<Post>>() {
        });
        response.close();

        // Check status
        assertEquals(403, response.getStatus());
    }
}
