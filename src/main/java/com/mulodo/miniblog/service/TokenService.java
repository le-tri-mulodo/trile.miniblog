/**
 * 
 */
package com.mulodo.miniblog.service;

import com.mulodo.miniblog.pojo.Token;
import com.mulodo.miniblog.pojo.User;

/**
 * @author TriLe
 */
public interface TokenService extends CommonService<Token> {

    /**
     * Create new token for User
     * 
     * @param userID
     *            Id of user
     * @return new token
     */
    Token createNewToken(User user);

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
