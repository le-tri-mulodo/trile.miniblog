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
 */
@Repository
public class UserDAOImpl extends CommonDAOImpl<User> implements UserDAO {

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
        // Set select fields
        // ProjectionList prjection = Projections.projectionList();
        // prjection.add(Projections.property("id"));
        // prjection.add(Projections.property("userName"));
        // prjection.add(Projections.property("firstName"));
        // prjection.add(Projections.property("lastName"));
        // prjection.add(Projections.property("avatarLink"));
        // prjection.add(Projections.property("joinDate"));
        // cr.setProjection(prjection);

        return (List<User>) cr.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkUserNameExist(String username) {
        Session session = sf.getCurrentSession();
        // Query to check username exist not select any fields
        Query query = session
                .createQuery("select count(*) from User u where u.userName = :username");
        query.setString("username", username);
        // Return true if username existed in db
        return (0 < (long) query.uniqueResult());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User update(User user) {
        Session session = sf.getCurrentSession();
        // Load from Hibernate term
        session.merge(user);
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkPassword(int user_id, String passhash) {
        Session session = sf.getCurrentSession();

        Query query = session
                .createQuery("select count(*) from User u where u.id = :user_id and u.passHash = :passHash");
        query.setInteger("user_id", user_id);
        query.setString("passHash", passhash);
        // Return true if username existed in db
        return (0 < (long) query.uniqueResult());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User checkPasswordGetUserInfo(String userName, String passhash) {
        Session session = sf.getCurrentSession();

        Query query = session
                .createQuery("from User u where u.userName = :userName and u.passHash = :passHash");
        query.setString("userName", userName);
        query.setString("passHash", passhash);
        // Return true if username existed in db
        return (User) query.uniqueResult();
    }
}
