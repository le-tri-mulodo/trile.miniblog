/**
 * 
 */
package com.mulodo.miniblog.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.mulodo.miniblog.dao.PostDAO;
import com.mulodo.miniblog.pojo.Post;

/**
 * @author TriLe
 */
@Repository
public class PostDAOImpl extends CommonDAOImpl<Post> implements PostDAO
{

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkOwner(int postId, int userId)
    {
        Session session = sf.getCurrentSession();

        Query query = session.createQuery("SELECT COUNT(*) FROM Post p WHERE p.id = :postId "
                + "AND p.user.id = :userId");
        // Set postId
        query.setInteger("postId", postId);
        // Set userId
        query.setInteger("userId", userId);
        // Return true if have record in db and otherwise
        return (0 < (long) query.uniqueResult());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Post> list()
    {
        Session session = sf.getCurrentSession();
        Query listQuery = session.createQuery("FROM Post p WHERE p.publicTime IS NOT NULL "
                + "ORDER BY createTime DESC");
        return listQuery.list();
    }
}
