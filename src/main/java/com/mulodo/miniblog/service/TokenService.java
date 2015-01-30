/**
 * 
 */
package com.mulodo.miniblog.service;

import com.mulodo.miniblog.pojo.User;

/**
 * @author TriLe
 *
 */
public interface TokenService extends CommonService<User> {

    /**
     * Create new token for User
     * @param userID
     * @return new token
     */
    String createNewToken(int userID);
}
