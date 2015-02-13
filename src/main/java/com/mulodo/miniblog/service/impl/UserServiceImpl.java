/**
 * 
 */
package com.mulodo.miniblog.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mulodo.miniblog.common.Util;
import com.mulodo.miniblog.dao.UserDAO;
import com.mulodo.miniblog.pojo.Token;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.TokenService;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 */
@Service
public class UserServiceImpl implements UserService
{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TokenService tokenSer;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User add(User user)
    {
        // Tolower case username
        user.setUserName(user.getUserName().toLowerCase());
        // Hash password
        user.setPassHash(Util.hashSHA256(user.getPassHash()));

        // Insert into db
        User result = userDAO.add(user);

        // Call token service to create and insert token into db
        Token token = tokenSer.createNewToken(user);
        // Set token to response
        result.setToken(token.getValue());

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User update(User updateUser)
    {
        // Update user
        // Load from Hibernate term userId
        // User user = load(updateUser.getId());
        // Change to get because response User info
        User user = userDAO.get(updateUser.getId());
        // change flag
        boolean changeFlag = false;
        // Check have change to set firstname, lastname, avatarlink
        if (null != updateUser.getFirstName()
                && !updateUser.getFirstName().equals(user.getFirstName())) {
            user.setFirstName(updateUser.getFirstName());
            changeFlag = true;
        }
        if (null != updateUser.getLastName()
                && !updateUser.getLastName().equals(user.getLastName())) {
            user.setLastName(updateUser.getLastName());
            changeFlag = true;
        }
        if (null != updateUser.getAvatarLink()
                && !updateUser.getAvatarLink().equals(user.getAvatarLink())) {
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
    @Transactional
    public void delete(User entity)
    {
        userDAO.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<User> search(String query)
    {
        return userDAO.search(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public boolean checkUserNameExist(String username)
    {
        // Check username existed
        return userDAO.checkUserNameExist(username);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public User get(int id)
    {
        return userDAO.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User load(int id)
    {
        return userDAO.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkPassword(int user_id, String password)
    {
        // Check username and passhash
        return userDAO.checkPassword(user_id, Util.hashSHA256(password));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User changePassword(int userId, String newPassword)
    {
        // Change password
        User user = load(userId);
        // Hash password
        user.setPassHash(Util.hashSHA256(newPassword));
        // Save to db
        userDAO.update(user);

        // Delete tokens of user
        tokenSer.deleteTokenByUserId(userId);
        // Create new token
        Token token = tokenSer.createNewToken(user);

        // Create new user to response
        User result = new User();
        // Set Id
        result.setId(userId);
        // Set token to response
        result.setToken(token.getValue());
        // Remove joindate
        result.setJoinDate(null);

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteAll()
    {
        userDAO.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User checkPasswordGetUserInfo(String userName, String password)
    {
        return userDAO.checkPasswordGetUserInfo(userName, Util.hashSHA256(password));
    }
}
