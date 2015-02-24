/**
 * 
 */
package com.mulodo.miniblog.service;

import java.util.List;

/**
 * @author TriLe
 */
public interface CommonService<T>
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
     * @return <b>FALSE</b> If entity <b>NOT</b> exist
     */
    boolean delete(T entity);

    /**
     * Load object from Hibernate term with id
     * 
     * @param id
     * @return proxy with input id is exist
     */
    T load(int id);

    /**
     * Get object from DB with id
     * 
     * @param id
     * @return entity with input id is exist
     */
    T get(int id);

    /**
     * Delete all rows in table
     */
    void deleteAll();

    /**
     * @return All entity in Db
     */
    List<T> list();
}
