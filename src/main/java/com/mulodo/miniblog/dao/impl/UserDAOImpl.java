/**
 * 
 */
package com.mulodo.miniblog.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
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

    @SuppressWarnings("unchecked")
    @Override
    public List<User> search(String querry) {
	String likeQuerryString = "%" + querry + "%";
	Criterion userName = Restrictions.ilike("userName", likeQuerryString);
	Criterion firstName = Restrictions.ilike("firstName", likeQuerryString);
	Criterion lastName = Restrictions.ilike("lastName", likeQuerryString);
	// OR operation
	LogicalExpression nameLE = Restrictions.or(firstName, lastName);

	Session session = sf.getCurrentSession();
	Criteria cr = session.createCriteria(genericType);
	cr.add(Restrictions.or(userName, nameLE));

	return (List<User>) cr.list();
    }

}
