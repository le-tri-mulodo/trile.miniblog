/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mulodo.miniblog.common.Contants;
import com.mulodo.miniblog.message.ResultMessage;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class UserControllerTest {

    @Autowired
    private UserService userSer;
    ResteasyClient client = new ResteasyClientBuilder().build();
    String ROOT_URL = "http://localhost:8080/miniblog.api";
    String USER_URL = ROOT_URL + Contants.URL_USER;

    @Test
    public void testAddNomal() {
	String url = USER_URL + "/search/huhu";
	System.out.println(url);
	ResteasyWebTarget target = client.target(url);

	Response response = target.request().get();
	// Read the entity
	ResultMessage<List<User>> result = response.readEntity(new GenericType<ResultMessage<List<User>>>() {
	});
	response.close();

	List<User> users = result.getData();
	Collections.sort(users);

	List<User> usersDb = userSer.search("huhu");
	Collections.sort(usersDb);

	for (User u : users) {
	    System.out.printf("%s - %s - %s - %s\n", u.getFirstName(), u.getLastName(), u.getUserName(),
		    u.getAvatarLink());
	}
	System.out.println("------------");
	for (User u : usersDb) {
	    System.out.printf("%s - %s - %s - %s\n", u.getFirstName(), u.getLastName(), u.getUserName(),
		    u.getAvatarLink());
	}
	// System.out.println(usersDb.toString());
	Assert.assertEquals(users, usersDb);

    }
}
