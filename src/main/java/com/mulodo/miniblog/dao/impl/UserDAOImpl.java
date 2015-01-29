/**
 * 
 */
package com.mulodo.miniblog.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mulodo.miniblog.dao.UserDAO;
import com.mulodo.miniblog.pojo.User;

/**
 * @author TriLe
 *
 */
@Repository
public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO {

    public List<User> list() {
	return list();
    }

}
