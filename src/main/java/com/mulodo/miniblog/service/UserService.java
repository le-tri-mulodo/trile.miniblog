/**
 * 
 */
package com.mulodo.miniblog.service;

import java.util.List;

import com.mulodo.miniblog.pojo.User;

/**
 * @author TriLe
 */
public interface UserService extends CommonService<User> {

    List<User> search(String query);

    boolean checkUserNameExist(String username);

    boolean checkPassword(int user_id, String password);

    User changePassword(int user_id, String newPassword);

}
