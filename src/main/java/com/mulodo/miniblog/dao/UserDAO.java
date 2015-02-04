package com.mulodo.miniblog.dao;

import java.util.List;

import com.mulodo.miniblog.pojo.User;

public interface UserDAO extends GenericDAO<User> {

    List<User> search(String query);

    boolean checkUserNameExist(User user);
}
