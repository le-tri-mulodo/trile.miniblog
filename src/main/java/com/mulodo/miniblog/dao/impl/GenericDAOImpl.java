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
import org.springframework.transaction.annotation.Transactional;

import com.mulodo.miniblog.dao.GenericDAO;

/**
 * @author TriLe
 *
 */
public abstract class GenericDAOImpl<T> implements GenericDAO<T> {

    @Autowired
    protected SessionFactory sf;

    private String T_TYPE;

    public GenericDAOImpl() {
	@SuppressWarnings("unchecked")
	Class<T> genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), GenericDAOImpl.class);
	T_TYPE = "from " + genericType.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mulodo.miniblog.dao.GenericDao#add(T)
     */
    @Override
    @Transactional
    public void add(T entity) {
	Session session = sf.getCurrentSession();
	session.persist(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mulodo.miniblog.dao.GenericDao#update(T)
     */
    @Override
    @Transactional
    public void update(T entity) {
	Session session = sf.getCurrentSession();
	session.update(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mulodo.miniblog.dao.GenericDao#delete(T)
     */
    @Override
    @Transactional
    public void delete(T entity) {
	Session session = sf.getCurrentSession();
	session.delete(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mulodo.miniblog.dao.GenericDao#list(java.lang.String)
     */
    @Override
    @Transactional
    public List<T> list() {
	Session session = sf.getCurrentSession();
	Query listQuery = session.createQuery("from " + T_TYPE);
	return listQuery.list();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.mulodo.miniblog.dao.GenericDao#setSf(org.hibernate.SessionFactory)
     */
    @Override
    public void setSf(SessionFactory sf) {
	this.sf = sf;
    }
}
