/**
 * 
 */
package com.mulodo.miniblog.service;

import java.util.List;

import com.mulodo.miniblog.pojo.User;

/**
 * @author TriLe
 *
 */
public interface UserService extends GenericService<User> {

    List<User> search(String query);

    boolean checkUserNameExist(User user);
}
