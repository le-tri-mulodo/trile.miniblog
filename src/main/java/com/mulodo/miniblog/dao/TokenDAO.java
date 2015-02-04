/**
 * 
 */
package com.mulodo.miniblog.dao;

import com.mulodo.miniblog.pojo.Token;

/**
 * @author TriLe
 *
 */
public interface TokenDAO extends GenericDAO<Token> {

    boolean checkToken(int userId, String token);
}
