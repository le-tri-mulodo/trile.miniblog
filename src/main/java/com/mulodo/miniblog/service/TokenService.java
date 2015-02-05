/**
 * 
 */
package com.mulodo.miniblog.service;

import com.mulodo.miniblog.pojo.Token;
import com.mulodo.miniblog.pojo.User;

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
    Token createNewToken(User user);

    boolean checkToken(int userId, String token);
    
    int deleteTokenByUserId(int userId);
}
