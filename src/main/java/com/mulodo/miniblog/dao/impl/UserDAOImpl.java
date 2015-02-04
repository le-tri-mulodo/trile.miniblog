/**
 * 
 */
package com.mulodo.miniblog.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mulodo.miniblog.dao.UserDAO;
import com.mulodo.miniblog.pojo.User;

/**
 * @author TriLe
 *
 */
@Repository
public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<User> search(String query) {
	String likeQueryString = "%" + query + "%";
	Criterion userName = Restrictions.ilike("userName", likeQueryString);
	Criterion firstName = Restrictions.ilike("firstName", likeQueryString);
	Criterion lastName = Restrictions.ilike("lastName", likeQueryString);
	// OR operation
	LogicalExpression nameLE = Restrictions.or(firstName, lastName);

	Session session = sf.getCurrentSession();
	Criteria cr = session.createCriteria(GENERIC_TYPE);
	cr.add(Restrictions.or(userName, nameLE));

	return (List<User>) cr.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkUserNameExist(User user) {
	Session session = sf.getCurrentSession();
	// Query to check username exist not select any fields
	Query query = session.createQuery("select count(*) from User u where u.userName = :username");
	query.setString("username", user.getUserName());
	// Return true if username existed in db
	return (0 < (long) query.uniqueResult());
    }

    @Override
    public User update(User user) {
	Session session = sf.getCurrentSession();
	// Load from Hibernate term
	session.merge(user);
	return user;
    }

}
