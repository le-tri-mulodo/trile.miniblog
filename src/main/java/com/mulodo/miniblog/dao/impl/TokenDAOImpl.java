/**
 * 
 */
package com.mulodo.miniblog.dao.impl;

import java.sql.Timestamp;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.mulodo.miniblog.dao.TokenDAO;
import com.mulodo.miniblog.pojo.Token;

/**
 * @author TriLe
 */
@Repository
public class TokenDAOImpl extends CommonDAOImpl<Token> implements TokenDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkToken(int userId, String token) {
        Session session = sf.getCurrentSession();
        // Criteria cr = session.createCriteria(GENERIC_TYPE.getClass());
        //
        // // Check user_id and token match in database
        // cr.add(Restrictions.eq("user.id", userID));
        // cr.add(Restrictions.eq("value", token));
        // // Check expired time greater equal current time
        // cr.add(Restrictions.ge("expiredTime", new
        // Timestamp(System.currentTimeMillis())));
        // // Count row
        // cr.setProjection(Projections.rowCount());
        Query query = session
                .createQuery("select count(*) from Token t where t.user.id = :userId and t.value = :token and t.expiredTime >= :currentTime");
        // Set userId
        query.setInteger("userId", userId);
        // Set token
        query.setString("token", token);
        // Set current time to compare with expired time
        query.setTimestamp("currentTime", new Timestamp(System.currentTimeMillis()));
        // Return true if have record in db and otherwise
        return (0 < (long) query.uniqueResult());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteTokenByUserId(int userId) {
        Session session = sf.getCurrentSession();

        Query query = session.createQuery("delete from Token t where t.user.id = :userId");
        query.setInteger("userId", userId);

        return query.executeUpdate();
    }
}
