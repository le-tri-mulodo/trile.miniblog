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

    /**
     * Search in username, first name, last name contian query string
     * 
     * @param query
     *            Query to search username, first name, last name contain
     * @return list of users match with query
     */
    List<User> search(String query);

    /**
     * Check username existed in db
     * 
     * @param username
     *            Username
     * @return <b>TRUE</b> if user name existed in Db
     */
    boolean checkUserNameExist(String username);

    /**
     * Check password with user id valid
     * 
     * @param user_id
     *            Id of user
     * @param password
     *            Password
     * @return <b>TRUE</b> if a pair User id and Password hashed valid
     */
    boolean checkPassword(int user_id, String password);

    /**
     * Change password of user, delete all token of user and then create new
     * token
     * 
     * @param user_id
     *            Id of user
     * @param newPassword
     *            New password
     * @return User info after change password (include new token)
     */
    User changePassword(int user_id, String newPassword);

    /**
     * Check password with user name. If valid then return user info match with
     * input user name
     * 
     * @param userName
     *            Name of user
     * @param password
     *            Password hashed
     * @return <b>User</b> if a pair User id and Password hashed valid <br>
     *         <b>NULL</b> if username and password invalid
     */
    User checkPasswordGetUserInfo(String userName, String password);
}
