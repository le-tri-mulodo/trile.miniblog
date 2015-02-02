/**
 * 
 */
package com.mulodo.miniblog.service;

import com.mulodo.miniblog.pojo.Token;

/**
 * @author TriLe
 *
 */
public interface TokenService extends CommonService<Token> {

    /**
     * Create new token for User
     * 
     * @param userID
     * @return new token
     */
    String createNewToken(int userID);
}
