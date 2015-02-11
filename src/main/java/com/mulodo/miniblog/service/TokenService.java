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

    // /**
    // * Delete token with input token value match value in Db
    // *
    // * @param value
    // * value of token Id of user
    // * @return number of token deleted
    // */
    // int deleteToken(String value);

    /**
     * Create new token if input username and password valid
     * 
     * @param user
     *            User info include username and password
     * @return Token info
     */
    Token login(User user);

    /**
     * Delete valid token in Db
     * 
     * @param token
     *            Token value which deleted in Db
     * @return Logout status
     */
    boolean logout(String token);
}
