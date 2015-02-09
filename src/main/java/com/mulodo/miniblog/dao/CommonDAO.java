package com.mulodo.miniblog.dao;

import java.util.List;

public interface CommonDAO<T> {

    /**
     * Insert entity into DB
     * 
     * @param entity
     */
    T add(T entity);

    /**
     * Update entity in DB
     * 
     * @param entity
     */
    T update(T entity);

    /**
     * Delete entity from DB
     * 
     * @param entity
     */
    void delete(T entity);

    /**
     * @return
     */
    List<T> list();

    /**
     * Check entity is exist in DB
     * 
     * @param id
     * @return entity with input id is exist
     */
    boolean checkExist(int id);

    T get(int id);

    T load(int id);

    void deleteAll();
}