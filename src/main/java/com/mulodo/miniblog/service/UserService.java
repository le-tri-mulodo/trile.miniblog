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
public interface UserService extends CommonService<User> {

    List<User> list();
}
