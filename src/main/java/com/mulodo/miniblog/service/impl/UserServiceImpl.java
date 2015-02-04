/**
 * 
 */
package com.mulodo.miniblog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mulodo.miniblog.dao.UserDAO;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 *
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public User add(User entity) {
	return userDAO.add(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User update(User updateUser) {
	// Update user
	// Load from Hibernate term userId
	// User user = load(updateUser.getId());
	// Change to get because response User info
	User user = get(updateUser.getId());
	// change flag
	boolean changeFlag = false;
	// Check have change to set firstname, lastname, avatarlink
	if (null != updateUser.getFirstName() && !user.getFirstName().equals(updateUser.getFirstName())) {
	    user.setFirstName(updateUser.getFirstName());
	    changeFlag = true;
	}
	if (null != updateUser.getLastName() && !user.getLastName().equals(updateUser.getLastName())) {
	    user.setLastName(updateUser.getLastName());
	    changeFlag = true;
	}
	if (null != updateUser.getAvatarLink() && !user.getAvatarLink().equals(updateUser.getAvatarLink())) {
	    user.setAvatarLink(updateUser.getAvatarLink());
	    changeFlag = true;
	}

	if (changeFlag) {
	    // Update user in db
	    user = userDAO.update(user);
	    // Remove token when response
	    user.setToken(null);
	}

	return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(User entity) {
	userDAO.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> search(String query) {
	return userDAO.search(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkUserNameExist(User user) {
	// Check username existed
	return userDAO.checkUserNameExist(user);

    }

    @Override
    public User get(int id) {
	return userDAO.get(id);
    }

    @Override
    public User load(int id) {
	return userDAO.load(id);
    }
}
