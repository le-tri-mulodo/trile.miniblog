package com.mulodo.miniblog.dao;

import java.util.List;

import com.mulodo.miniblog.pojo.User;

public interface UserDAO extends CommonDAO<User> {

    List<User> search(String query);

    boolean checkUserNameExist(String username);

    boolean checkPassword(int user_id, String passhash);

}
