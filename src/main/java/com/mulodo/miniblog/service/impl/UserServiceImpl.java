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
    public void add(User entity) {
	userDAO.add(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(User entity) {
	userDAO.update(entity);
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
}
