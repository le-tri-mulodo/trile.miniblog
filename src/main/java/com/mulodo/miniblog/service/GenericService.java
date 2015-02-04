/**
 * 
 */
package com.mulodo.miniblog.service;

/**
 * @author TriLe
 *
 */
public interface GenericService<T> {

    T add(T entity);

    T update(T entity);

    void delete(T entity);

    T load(int id);
    
    T get(int id);
}
