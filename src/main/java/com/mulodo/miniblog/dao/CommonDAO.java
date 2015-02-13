package com.mulodo.miniblog.dao;

import java.util.List;

public interface CommonDAO<T>
{

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
     * @return <b>TRUE</b> if entity existed in Db
     */
    boolean checkExist(int id);

    /**
     * Get object from DB with id
     * 
     * @param id
     * @return entity with input id is exist
     */
    T get(int id);

    /**
     * Load object from Hibernate term with id
     * 
     * @param id
     * @return proxy with input id is exist
     */
    T load(int id);

    /**
     * Delete all rows in table
     */
    void deleteAll();
}