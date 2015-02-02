/**
 * 
 */
package com.mulodo.miniblog.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import com.mulodo.miniblog.dao.GenericDAO;

/**
 * @author TriLe
 *
 */
public class GenericDAOImpl<T> implements GenericDAO<T> {

    @Autowired
    protected SessionFactory sf;

    protected String T_TYPE;
    protected Class<T> genericType;

    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
	this.genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), GenericDAOImpl.class);
	this.T_TYPE = "from " + genericType.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T add(T entity) {
	Session session = sf.getCurrentSession();
	session.persist(entity);
	return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T update(T entity) {
	Session session = sf.getCurrentSession();
	session.update(entity);
	return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(T entity) {
	Session session = sf.getCurrentSession();
	session.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkExist(int id) {
	Session session = sf.getCurrentSession();
	return (null != session.get(genericType, id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> list() {
	Session session = sf.getCurrentSession();
	Query listQuery = session.createQuery("from " + T_TYPE);
	return listQuery.list();
    }

    public void setSf(SessionFactory sf) {
	this.sf = sf;
    }

}
