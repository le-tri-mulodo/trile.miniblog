/**
 * 
 */
package com.mulodo.miniblog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mulodo.miniblog.dao.UserDAO;
import com.mulodo.miniblog.message.ErrorMessage;
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
    public User update(User entity) {
	return userDAO.update(entity);
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
    public List<User> search(String querry) {
	return userDAO.search(querry);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ErrorMessage validate(User user) {
	// Check username
	// if (StringUtils.isNotEmpty(user.getUserName())) {
	// return new ErrorMessage(1, "Input validation failed",
	// "Username is required");
	// }
	// // Check password
	// if (StringUtils.isNotEmpty(user.getPassHash())) {
	// return new ErrorMessage(1, "Input validation failed",
	// "Password is required");
	// }
	// // Check firstname
	// if (StringUtils.isNotEmpty(user.getFirstName())) {
	// return new ErrorMessage(1, "Input validation failed",
	// "FirstName is required");
	// }
	// // Check lastname
	// if (StringUtils.isNotEmpty(user.getFirstName())) {
	// return new ErrorMessage(1, "Input validation failed",
	// "LastName is required");
	// }

	return null;
    }
}
