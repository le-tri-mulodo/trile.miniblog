/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import com.mulodo.miniblog.pojo.Token;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.TokenService;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class TokenControllerTest
{

    @Autowired
    private UserService userSer;
    @Autowired
    private TokenService tokenSer;

    ResteasyClient client = new ResteasyClientBuilder().build();
    String ROOT_URL = "http://localhost:8080/miniblog.api";
    String TOKEN_URL = ROOT_URL + Contants.URL_TOKEN;

    @Before
    public void prepareData()
    {
        // Delete all data
        tokenSer.deleteAll();
        userSer.deleteAll();
    }

    private Token createDummyData()
    {
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
    public void testLogin()
    {
        createDummyData();

        ResteasyWebTarget target = client.target(TOKEN_URL + Contants.URL_LOGIN);

        Form form = new Form();
        form.param("username", "thanhtri");
        form.param("password", "password");

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Token> result = response.readEntity(new GenericType<ResultMessage<Token>>() {
        });
        response.close();

        // Check status
        assertEquals(200, response.getStatus());
        // Check token not null
        assertNotNull(result.getData().getValue());
    }

    // Miss username
    @Test
    public void testLogin2()
    {
        createDummyData();

        ResteasyWebTarget target = client.target(TOKEN_URL + Contants.URL_LOGIN);

        Form form = new Form();
        form.param("password", "password");

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Token> result = response.readEntity(new GenericType<ResultMessage<Token>>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss password
    @Test
    public void testLogin3()
    {
        createDummyData();

        ResteasyWebTarget target = client.target(TOKEN_URL + Contants.URL_LOGIN);

        Form form = new Form();
        form.param("username", "thanhtri");

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Token> result = response.readEntity(new GenericType<ResultMessage<Token>>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Username password invalid
    @Test
    public void testLogin4()
    {
        createDummyData();

        ResteasyWebTarget target = client.target(TOKEN_URL + Contants.URL_LOGIN);

        Form form = new Form();
        form.param("username", "thanhtri");
        form.param("password", "invalidpassword");

        Response response = target.request().post(Entity.form(form));

        ResultMessage<Token> result = response.readEntity(new GenericType<ResultMessage<Token>>() {
        });
        response.close();

        // Check status
        assertEquals(401, response.getStatus());
    }

    // Normal
    @Test
    public void testLogout()
    {
        Token token = createDummyData();

        ResteasyWebTarget target = client.target(TOKEN_URL + Contants.URL_LOGOUT);

        Form form = new Form();
        form.param("token", token.getValue());

        Response response = target.request().post(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(200, response.getStatus());
    }

    // Miss token
    @Test
    public void testLogout2()
    {
        Token token = createDummyData();

        ResteasyWebTarget target = client.target(TOKEN_URL + Contants.URL_LOGOUT);

        Response response = target.request().post(null);

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Miss token size < 64
    @Test
    public void testLogout3()
    {
        Token token = createDummyData();

        ResteasyWebTarget target = client.target(TOKEN_URL + Contants.URL_LOGOUT);

        Form form = new Form();
        form.param("token", token.getValue().substring(32));

        Response response = target.request().post(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Token invalid
    @Test
    public void testLogout4()
    {
        Token token = createDummyData();

        ResteasyWebTarget target = client.target(TOKEN_URL + Contants.URL_LOGOUT);

        Form form = new Form();
        // Reverse token to create invalid token
        form.param("token", StringUtils.reverse(token.getValue()));

        Response response = target.request().post(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(401, response.getStatus());
    }
}
