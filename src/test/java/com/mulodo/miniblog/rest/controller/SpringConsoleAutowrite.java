/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 *
 */
@Component
public class SpringConsoleAutowrite {

    @Autowired
    private UserService userSer;

    private void start(String[] args) {
	User user = new User();
	// Set username
	user.setUserName("unittest");
	// Set password
	user.setPassHash("unittest");
	// Set firstname
	user.setFirstName("unittest");
	// Set lastname
	user.setLastName("unittest");
	// Set Avatarlink
	user.setAvatarLink("unittest");

	// Call user service to insert into db
	user = userSer.add(user);
    }

    public static void main(String[] args) {
	ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

	SpringConsoleAutowrite p = context.getBean(SpringConsoleAutowrite.class);
	p.start(args);
    }
}
