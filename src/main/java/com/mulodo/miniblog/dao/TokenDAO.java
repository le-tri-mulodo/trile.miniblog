/**
 * 
 */
package com.mulodo.miniblog.dao;

import com.mulodo.miniblog.pojo.Token;

/**
 * @author TriLe
 */
public interface TokenDAO extends CommonDAO<Token> {

    /**
     * Check token valid
     * 
     * @param userId
     *            Id of user
     * @param token
     *            The value of token
     * @return <b>TRUE</b> if a pair User Id and Token valid
     */
    boolean checkToken(int userId, String token);

    /**
     * Delete all token of user by id
     * 
     * @param userId
     *            Id of user
     * @return number of token deleted
     */
    int deleteTokenByUserId(int userId);
}
