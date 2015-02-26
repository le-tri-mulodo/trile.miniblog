/**
 * 
 */
package com.mulodo.miniblog.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
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
        // Select all public posts ignore content and editTime
        Query listQuery = session.createQuery(
                "SELECT p.id AS id, p.userId AS userId, p.title AS title, "
                        + "p.description AS description, p.createTime AS createTime, "
                        + "p.publicTime AS publicTime"
                        + " FROM Post p WHERE p.publicTime IS NOT NULL ORDER BY p.createTime DESC")
        // Set Transformer to convert Object to Post class
                .setResultTransformer(Transformers.aliasToBean(Post.class));
        return listQuery.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Post> getByUserId(int userId)
    {
        Session session = sf.getCurrentSession();
        // Select all public posts of user, ignore content and editTime
        Query listQuery = session.createQuery(
                "SELECT p.id AS id, p.userId AS userId, p.title AS title, "
                        + "p.description AS description, p.createTime AS createTime, "
                        + "p.publicTime AS publicTime"
                        + " FROM Post p WHERE p.publicTime IS NOT NULL "
                        + "AND p.userId = :userId ORDER BY createTime DESC")
        // Set Transformer to convert Object to Post class
                .setResultTransformer(Transformers.aliasToBean(Post.class));
        // Set userId
        listQuery.setInteger("userId", userId);
        return listQuery.list();
    }
}
