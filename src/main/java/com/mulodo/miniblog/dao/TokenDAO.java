/**
 * 
 */
package com.mulodo.miniblog.dao;

import com.mulodo.miniblog.pojo.Token;

/**
 * @author TriLe
 */
public interface TokenDAO extends CommonDAO<Token> {

    boolean checkToken(int userId, String token);

    int deleteTokenByUserId(int userId);
}
