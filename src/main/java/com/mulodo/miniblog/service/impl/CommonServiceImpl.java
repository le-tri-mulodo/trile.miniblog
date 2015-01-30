/**
 * 
 */
package com.mulodo.miniblog.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.mulodo.miniblog.service.CommonService;

/**
 * @author TriLe
 *
 */
@Transactional
public class CommonServiceImpl<T> implements CommonService<T> {

    @Override
    public void add(T entity) {

    }

    @Override
    public void update(T entity) {

    }

    @Override
    public void delete(T entity) {

    }

}
