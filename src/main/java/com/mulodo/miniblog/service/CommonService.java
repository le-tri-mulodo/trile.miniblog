/**
 * 
 */
package com.mulodo.miniblog.service;

/**
 * @author TriLe
 *
 */
public interface CommonService<T> {

    void add(T entity);

    void update(T entity);

    void delete(T entity);
}
