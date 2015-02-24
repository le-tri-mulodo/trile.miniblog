/**
 * 
 */
package com.mulodo.miniblog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mulodo.miniblog.dao.TokenDAO;
import com.mulodo.miniblog.pojo.Token;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.TokenService;

/**
 * @author TriLe
 *
 */
/**
 * @author TriLe
 */
@Service
public class TokenServiceImpl implements TokenService
{

    @Autowired
    private TokenDAO tokenDAO;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Token add(Token entity)
    {
        return tokenDAO.add(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Token update(Token entity)
    {
        return tokenDAO.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean delete(Token entity)
    {
        tokenDAO.delete(entity);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Token createNewToken(User user)
    {
        // Create new token
        Token token = new Token(user);
        // Insert into DB
        token = add(token);

        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean checkToken(int userId, String token)
    {
        return tokenDAO.checkToken(userId, token);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Token load(int id)
    {
        return tokenDAO.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Token get(int id)
    {
        return tokenDAO.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public int deleteTokenByUserId(int userId)
    {
        return tokenDAO.deleteTokenByUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteAll()
    {
        tokenDAO.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Token login(User user)
    {
        if (null == user) {
            return null;
        }

        // Call token service to create and insert token into db
        Token token = createNewToken(user);
        // Set username and userId
//        token.setUserid(user.getId());
        token.setUserName(user.getUserName());

        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean logout(String token)
    {
        return tokenDAO.deleteToken(token);
    }

    @Override
    public List<Token> list()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
