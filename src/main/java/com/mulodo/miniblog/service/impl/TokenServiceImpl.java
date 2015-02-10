/**
 * 
 */
package com.mulodo.miniblog.service.impl;

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
@Transactional
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenDAO tokenDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public Token add(Token entity) {
        return tokenDAO.add(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Token update(Token entity) {
        return tokenDAO.update(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Token entity) {
        tokenDAO.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Token createNewToken(User user) {
        // Create new token
        Token token = new Token(user);
        // Insert into DB
        token = add(token);

        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkToken(int userId, String token) {
        return tokenDAO.checkToken(userId, token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Token load(int id) {
        return tokenDAO.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Token get(int id) {
        return tokenDAO.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTokenByUserId(int userId) {
        return tokenDAO.deleteTokenByUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll() {
        tokenDAO.deleteAll();
    }
}
