/**
 * 
 */
package com.mulodo.miniblog.service;


/**
 * @author TriLe
 *
 */
public interface CommonService<T> {

    T add(T entity);

    T update(T entity);

    void delete(T entity);
}
