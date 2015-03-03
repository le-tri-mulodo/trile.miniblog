/**
 * 
 */
package com.mulodo.miniblog.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.mulodo.miniblog.dao.CommentDAO;
import com.mulodo.miniblog.pojo.Comment;

/**
 * Implement of DAO interface about Comment function
 * 
 * @author TriLe
 */
@Repository
public class CommentDAOImpl extends CommonDAOImpl<Comment> implements CommentDAO
{

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Comment> getByPostId(int postId)
    {
        Session session = sf.getCurrentSession();
        // Select all comments of post and sort by create time ascending
        Query listQuery = session
                .createQuery("FROM Comment c WHERE c.postId = :postId ORDER BY createTime ASC");
        // Set postId
        listQuery.setInteger("postId", postId);
        return listQuery.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> getByUserId(int userId)
    {
        Session session = sf.getCurrentSession();
        // Select all comments of post and sort by create time ascending
        Query listQuery = session
                .createQuery("FROM Comment c WHERE c.userId = :userId ORDER BY createTime ASC");
        // Set postId
        listQuery.setInteger("userId", userId);
        return listQuery.list();
    }
}
