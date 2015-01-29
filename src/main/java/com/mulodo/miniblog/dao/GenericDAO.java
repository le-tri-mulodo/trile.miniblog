package com.mulodo.miniblog.dao;

import java.util.List;

import org.hibernate.SessionFactory;

public interface GenericDAO<T> {

    public abstract void add(T entity);

    public abstract void update(T entity);

    public abstract void delete(T entity);

    public abstract List<T> list();

    /**
     * @param sf
     *            the sf to set
     */
    public abstract void setSf(SessionFactory sf);

}