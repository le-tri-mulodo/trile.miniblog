package com.mulodo.miniblog.dao;

import java.util.List;

public interface GenericDAO<T> {

    /**
     * Insert entity into DB
     * 
     * @param entity
     */
    public T add(T entity);

    /**
     * Update entity in DB
     * 
     * @param entity
     */
    public T update(T entity);

    /**
     * Delete entity from DB
     * 
     * @param entity
     */
    public void delete(T entity);

    /**
     * @return
     */
    public List<T> list();

    /**
     * Check entity is exist in DB
     * 
     * @param id
     * @return entity with input id is exist
     */
    public boolean checkExist(int id);

    // /**
    // * @param sf
    // * the sf to set
    // */
    // public void setSf(SessionFactory sf);

}