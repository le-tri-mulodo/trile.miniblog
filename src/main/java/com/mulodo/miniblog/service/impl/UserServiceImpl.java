/**
 * 
 */
package com.mulodo.miniblog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mulodo.miniblog.dao.UserDAO;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 *
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public void add(User entity) {
	userDAO.add(entity);
    }

    @Override
    public void update(User entity) {
	userDAO.update(entity);
    }

    @Override
    public void delete(User entity) {
	userDAO.delete(entity);
    }

    @Override
    public List<User> list() {
	return userDAO.list();
    }

}
