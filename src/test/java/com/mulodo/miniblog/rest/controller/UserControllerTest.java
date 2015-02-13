/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mulodo.miniblog.common.Contants;
import com.mulodo.miniblog.message.ResultMessage;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.PostService;
import com.mulodo.miniblog.service.TokenService;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class UserControllerTest
{
    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    private UserService userSer;
    @Autowired
    private TokenService tokenSer;
    @Autowired
    private PostService postSer;

    ResteasyClient client = new ResteasyClientBuilder().build();
    String ROOT_URL = "http://localhost:8080/miniblog.api";
    String USER_URL = ROOT_URL + Contants.URL_USER;

    @Before
    public void prepareData()
    {
        // Delete all data
        tokenSer.deleteAll();
        postSer.deleteAll();
        userSer.deleteAll();
    }

    @Test
    public void testAddUser()
    {
        ResteasyWebTarget target = client.target(USER_URL);

        // Create user
        User user = new User();
        user.setUserName("trile");
        user.setFirstName("Tri");
        user.setLastName("Le");
        user.setAvatarLink("trile.jpg");

        Form form = new Form();
        form.param("username", user.getUserName());
        form.param("firstname", user.getFirstName());
        form.param("lastname", user.getLastName());
        form.param("password", "password");
        form.param("avatarlink", user.getAvatarLink());

        Response response = target.request().post(Entity.form(form));

        ResultMessage<User> result = response.readEntity(new GenericType<ResultMessage<User>>() {
        });
        response.close();

        User rUser = result.getData();
        // Check status
        assertEquals(201, response.getStatus());
        // Check Id
        assertNotEquals(0, rUser.getId());
        // Check token
        assertNotNull(result.getData().getToken());
        // // Check join date
        // assertEquals(user.getJoinDate().getYear(),
        // rUser.getJoinDate().getYear());
        // assertEquals(user.getJoinDate().getMonth(),
        // rUser.getJoinDate().getMonth());
        // assertEquals(user.getJoinDate().getDay(),
        // rUser.getJoinDate().getDay());
        //
        // // Ignore join date
        // rUser.setJoinDate(null);
        // user.setJoinDate(null);

        // Assert
        assertEquals(user, rUser);
    }

    // User existed
    @Test
    public void testAddUser2()
    {
        // Add user
        User user = new User();
        user.setUserName("trile");
        user.setFirstName("Tri");
        user.setLastName("Le");
        user.setPassHash("test");
        user.setAvatarLink("test.jpg");
        userSer.add(user);

        ResteasyWebTarget target = client.target(USER_URL);
        // Create user
        user = new User();
        user.setUserName("trile");
        user.setFirstName("Tri");
        user.setLastName("Le");
        user.setAvatarLink("trile.jpg");

        Form form = new Form();
        form.param("username", user.getUserName());
        form.param("firstname", user.getFirstName());
        form.param("lastname", user.getLastName());
        form.param("password", "password");
        form.param("avatarlink", user.getAvatarLink());

        Response response = target.request().post(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        assertEquals(400, response.getStatus());
    }

    // Fields required
    @Test
    public void testAddUser3()
    {
        ResteasyWebTarget target = client.target(USER_URL);
        // Create user
        User user = new User();
        user = new User();
        user.setFirstName("Tri");
        user.setLastName("Le");
        user.setAvatarLink("trile.jpg");

        Form form = new Form();
        form.param("firstname", user.getFirstName());
        form.param("lastname", user.getLastName());
        form.param("password", "password");
        form.param("avatarlink", user.getAvatarLink());

        Response response = target.request().post(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        assertEquals(400, response.getStatus());
    }

    // Fields required
    @Test
    public void testAddUser4()
    {
        ResteasyWebTarget target = client.target(USER_URL + "/");
        // Create user
        User user = new User();
        user = new User();
        user.setUserName("trile");
        user.setFirstName("Tri");
        user.setLastName("Le");
        user.setAvatarLink("trile.jpg");

        Form form = new Form();
        form.param("username", user.getUserName());
        form.param("firstname", user.getFirstName());
        form.param("lastname", user.getLastName());
        form.param("avatarlink", user.getAvatarLink());

        Response response = target.request().post(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        assertEquals(400, response.getStatus());
    }

    // Fields required
    @Test
    public void testAddUser5()
    {
        ResteasyWebTarget target = client.target(USER_URL + "/");
        // Create user
        User user = new User();
        user = new User();
        user.setUserName("trile");
        user.setFirstName("Tri");
        user.setLastName("Le");
        user.setAvatarLink("trile.jpg");

        Form form = new Form();
        form.param("username", user.getUserName());
        form.param("lastname", user.getLastName());
        form.param("password", "password");
        form.param("avatarlink", user.getAvatarLink());

        Response response = target.request().post(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        assertEquals(400, response.getStatus());
    }

    // Fields required
    @Test
    public void testAddUser6()
    {
        ResteasyWebTarget target = client.target(USER_URL);
        // Create user
        User user = new User();
        user = new User();
        user.setUserName("trile");
        user.setFirstName("Tri");
        user.setLastName("Le");
        user.setAvatarLink("trile.jpg");

        Form form = new Form();
        form.param("username", user.getUserName());
        form.param("firstname", user.getFirstName());
        form.param("password", "password");
        form.param("avatarlink", user.getAvatarLink());

        Response response = target.request().post(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testSearch()
    {
        // Prepare data
        searchPrepare();

        String query = "tri";
        String url = USER_URL + "/search/" + query;
        ResteasyWebTarget target = client.target(url);

        Response response = target.request().get();
        // Read the entity
        ResultMessage<List<User>> result = response
                .readEntity(new GenericType<ResultMessage<List<User>>>() {
                });
        response.close();

        List<User> users = result.getData();
        Collections.sort(users);

        List<User> usersDb = userSer.search(query);
        Collections.sort(usersDb);

        // for (User u : users) {
        // System.out.printf("%s - %s - %s - %s\n", u.getFirstName(),
        // u.getLastName(), u.getUserName(),
        // u.getAvatarLink());
        // }
        // System.out.println("------------");
        // for (User u : usersDb) {
        // System.out.printf("%s - %s - %s - %s\n", u.getFirstName(),
        // u.getLastName(), u.getUserName(),
        // u.getAvatarLink());
        // }
        assertEquals(users, usersDb);

    }

    private void searchPrepare()
    {
        User user = new User();
        user.setUserName("trile");
        user.setFirstName("Tri");
        user.setLastName("Le");
        user.setPassHash("test");
        user.setAvatarLink("test.jpg");
        userSer.add(user);

        user = new User();
        user.setUserName("lethanh");
        user.setFirstName("Le");
        user.setLastName("ThanhTri");
        user.setPassHash("test");
        userSer.add(user);

        user = new User();
        user.setUserName("abcd");
        user.setFirstName("TRI");
        user.setLastName("Thanh");
        user.setPassHash("test");
        userSer.add(user);

        user = new User();
        user.setUserName("cdef");
        user.setFirstName("TRI");
        user.setLastName("Thanh");
        user.setPassHash("test");
        userSer.add(user);

        user = new User();
        user.setUserName("cdexxf");
        user.setFirstName("asdas");
        user.setLastName("czxc");
        user.setPassHash("cxz");
        userSer.add(user);

        user = new User();
        user.setUserName("asjdhaskjf");
        user.setFirstName("asxzdas");
        user.setLastName("ccrfzxc");
        user.setPassHash("cddxz");
        userSer.add(user);
    }

    // User not exist
    @Test
    public void testgetUserInfo()
    {

        // Create new user
        User user = new User();
        user.setUserName("trile");
        user.setFirstName("Tri");
        user.setLastName("Le");
        user.setPassHash("test");
        user.setAvatarLink("test.jpg");
        user = userSer.add(user);

        ResteasyWebTarget target = client.target(USER_URL + "/" + user.getId());

        Response response = target.request().get();
        ResultMessage<User> result = response.readEntity(new GenericType<ResultMessage<User>>() {
        });
        response.close();

        // Check status
        assertEquals(200, response.getStatus());

        User rUser = result.getData();
        // Check join date
        assertEquals(user.getJoinDate().getYear(), rUser.getJoinDate().getYear());
        assertEquals(user.getJoinDate().getMonth(), rUser.getJoinDate().getMonth());
        assertEquals(user.getJoinDate().getDay(), rUser.getJoinDate().getDay());

        // Ignore join date
        rUser.setJoinDate(null);
        user.setJoinDate(null);
        // Check user info
        assertEquals(user, rUser);
    }

    @Test
    public void testgetUserInfo2()
    {
        ResteasyWebTarget target = client.target(USER_URL + "/0");

        Response response = target.request().get();
        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    @Test
    public void changePassword()
    {
        ResteasyWebTarget target = client.target(USER_URL + Contants.URL_CHPWD);
        String currentPass = "currentpass";
        // Create new user
        User user = new User();
        user.setUserName("trile");
        user.setFirstName("Tri");
        user.setLastName("Le");
        user.setPassHash(currentPass);
        user.setAvatarLink("test.jpg");
        user = userSer.add(user);

        Form form = new Form();
        // Convert int to String to add user_id into x-form
        form.param("user_id", Integer.toString(user.getId()));
        form.param("currentpassword", currentPass);
        form.param("newpassword", "newpass");

        Response response = target.request().put(Entity.form(form));

        ResultMessage<User> result = response.readEntity(new GenericType<ResultMessage<User>>() {
        });
        response.close();
        // Check status
        assertEquals(200, response.getStatus());

        User rUser = result.getData();
        // Check check id
        assertEquals(user.getId(), rUser.getId());
        // Check token
        assertNotNull(rUser.getToken());
        assertTrue(tokenSer.checkToken(rUser.getId(), rUser.getToken()));
    }

    // User or password invaild
    @Test
    public void changePassword2()
    {
        ResteasyWebTarget target = client.target(USER_URL + Contants.URL_CHPWD);
        Form form = new Form();
        // Convert int to String to add user_id into x-form
        form.param("user_id", "123");
        form.param("currentpassword", "1213");
        form.param("newpassword", "newpass");

        Response response = target.request().put(Entity.form(form));

        ResultMessage<User> result = response.readEntity(new GenericType<ResultMessage<User>>() {
        });
        response.close();
        // Check status
        assertEquals(400, response.getStatus());
    }

    // Change firstname
    @Test
    public void testEditUser()
    {
        ResteasyWebTarget target = client.target(USER_URL);

        // Create user
        User user = new User();
        user = new User();
        user.setUserName("asjdhaskjf");
        user.setFirstName("asxzdas");
        user.setLastName("ccrfzxc");
        user.setPassHash("cddxz");
        userSer.add(user);

        String newFirstName = "changed";
        // Edit info
        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
        form.param("token", user.getToken());
        form.param("firstname", newFirstName);

        Response response = target.request().put(Entity.form(form));

        ResultMessage<User> result = response.readEntity(new GenericType<ResultMessage<User>>() {
        });
        response.close();

        User rUser = result.getData();
        // Check status
        assertEquals(200, response.getStatus());
        // Assert
        assertNotEquals(user, rUser);
        // Check firstname
        assertEquals(newFirstName, rUser.getFirstName());
    }

    // Change lastname
    @Test
    public void testEditUser2()
    {
        ResteasyWebTarget target = client.target(USER_URL);

        // Create user
        User user = new User();
        user = new User();
        user.setUserName("asjdhaskjf");
        user.setFirstName("asxzdas");
        user.setLastName("ccrfzxc");
        user.setPassHash("cddxz");
        userSer.add(user);

        String newLastName = "changed";
        // Edit info
        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
        form.param("token", user.getToken());
        form.param("lastname", newLastName);

        Response response = target.request().put(Entity.form(form));

        ResultMessage<User> result = response.readEntity(new GenericType<ResultMessage<User>>() {
        });
        response.close();

        User rUser = result.getData();
        // Check status
        assertEquals(200, response.getStatus());
        // Assert
        assertNotEquals(user, rUser);
        // Check lastname
        assertEquals(newLastName, rUser.getLastName());
    }

    // Change avatarlink
    /**
     * 
     */
    @Test
    public void testEditUser3()
    {
        ResteasyWebTarget target = client.target(USER_URL);

        // Create user
        User user = new User();
        user = new User();
        user.setUserName("asjdhaskjf");
        user.setFirstName("asxzdas");
        user.setLastName("ccrfzxc");
        user.setPassHash("cddxz");
        userSer.add(user);

        String newAvatarLink = "changed";
        // Edit info
        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
        form.param("token", user.getToken());
        form.param("avatarlink", newAvatarLink);

        Response response = target.request().put(Entity.form(form));

        ResultMessage<User> result = response.readEntity(new GenericType<ResultMessage<User>>() {
        });
        response.close();

        User rUser = result.getData();
        // Check status
        assertEquals(200, response.getStatus());
        // Assert
        assertNotEquals(user, rUser);
        // Check avatarlink
        assertEquals(newAvatarLink, rUser.getAvatarLink());
    }

    // Change all
    /**
     * 
     */
    @Test
    public void testEditUser4()
    {
        ResteasyWebTarget target = client.target(USER_URL);

        // Create user
        User user = new User();
        user = new User();
        user.setUserName("asjdhaskjf");
        user.setFirstName("asxzdas");
        user.setLastName("ccrfzxc");
        user.setPassHash("cddxz");
        userSer.add(user);

        String newAvatarLink = "changed";
        String newLastName = "changed";
        String newFirstName = "changed";
        // Edit info
        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
        form.param("token", user.getToken());
        form.param("avatarlink", newAvatarLink);
        form.param("lastname", newLastName);
        form.param("firstname", newFirstName);

        Response response = target.request().put(Entity.form(form));

        ResultMessage<User> result = response.readEntity(new GenericType<ResultMessage<User>>() {
        });
        response.close();

        User rUser = result.getData();
        // Check status
        assertEquals(200, response.getStatus());
        // Assert
        assertNotEquals(user, rUser);
        // Check avatarlink
        assertEquals(newAvatarLink, rUser.getAvatarLink());
        // Check lastname
        assertEquals(newLastName, rUser.getLastName());
        // Check firstname
        assertEquals(newFirstName, rUser.getFirstName());
    }

    // validate: miss token
    /**
     * 
     */
    @Test
    public void testEditUser5()
    {
        ResteasyWebTarget target = client.target(USER_URL);

        // Create user
        User user = new User();
        user = new User();
        user.setUserName("asjdhaskjf");
        user.setFirstName("asxzdas");
        user.setLastName("ccrfzxc");
        user.setPassHash("cddxz");
        userSer.add(user);

        String newAvatarLink = "changed";
        String newLastName = "changed";
        String newFirstName = "changed";
        // Edit info
        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
        form.param("avatarlink", newAvatarLink);
        form.param("lastname", newLastName);
        form.param("firstname", newFirstName);

        Response response = target.request().put(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(400, response.getStatus());
    }

    // Unauthorized: token invalid
    /**
     * 
     */
    @Test
    public void testEditUser6()
    {
        ResteasyWebTarget target = client.target(USER_URL);

        // Create user
        User user = new User();
        user = new User();
        user.setUserName("asjdhaskjf");
        user.setFirstName("asxzdas");
        user.setLastName("ccrfzxc");
        user.setPassHash("cddxz");
        userSer.add(user);

        String newAvatarLink = "changed";
        String newLastName = "changed";
        String newFirstName = "changed";
        // Edit info
        Form form = new Form();
        form.param("user_id", Integer.toString(user.getId()));
        // reverse token to create invalid token
        form.param("token", StringUtils.reverse(user.getToken()));
        form.param("avatarlink", newAvatarLink);
        form.param("lastname", newLastName);
        form.param("firstname", newFirstName);

        Response response = target.request().put(Entity.form(form));

        ResultMessage result = response.readEntity(new GenericType<ResultMessage>() {
        });
        response.close();

        // Check status
        assertEquals(401, response.getStatus());
    }
}
