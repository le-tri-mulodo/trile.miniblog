/**
 * 
 */
package com.mulodo.miniblog.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.mulodo.miniblog.common.Contants;
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
     * @return All public post in Db order by create time desc
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
    public List<Post> getByUserId(int userId, boolean showUnpublic)
    {
        Session session = sf.getCurrentSession();
        // Select all public posts of user, ignore content and editTime
        // Query listQuery = session.createQuery(
        // "SELECT p.id AS id, p.userId AS userId, p.title AS title, "
        // + "p.description AS description, p.createTime AS createTime, "
        // + "p.publicTime AS publicTime"
        // + " FROM Post p WHERE p.publicTime IS NOT NULL "
        // + "AND p.userId = :userId ORDER BY createTime DESC")
        // Set Transformer to convert Object to Post class
        Criteria cr = session.createCriteria(GENERIC_TYPE);

        // Set select fields
        ProjectionList prjection = Projections.projectionList();
        prjection.add(Projections.property("id"), "id");
        prjection.add(Projections.property("userId"), "userId");
        prjection.add(Projections.property("title"), "title");
        prjection.add(Projections.property("description"), "description");
        prjection.add(Projections.property("createTime"), "createTime");
        prjection.add(Projections.property("publicTime"), "publicTime");
        cr.setProjection(prjection);

        // Set userId
        cr.add(Restrictions.eq("userId", userId));
        // Public post or not
        if (!showUnpublic) {
            cr.add(Restrictions.isNotNull("publicTime"));
        }
        // Set Transformer to convert Object to Post class
        cr.setResultTransformer(Transformers.aliasToBean(Post.class));
        return (List<Post>) cr.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Post> search(String query)
    {
        String likeQueryString = "%" + query + "%";
        Criterion title = Restrictions.ilike("title", likeQueryString);
        Criterion description = Restrictions.ilike("description", likeQueryString);
        Criterion content = Restrictions.ilike("content", likeQueryString);
        // OR operation
        LogicalExpression nameLE = Restrictions.or(description, content);

        Session session = sf.getCurrentSession();
        Criteria cr = session.createCriteria(GENERIC_TYPE);
        cr.add(Restrictions.or(title, nameLE));
        // Set select fields
        // ProjectionList prjection = Projections.projectionList();
        // prjection.add(Projections.property("id"), "id");
        // prjection.add(Projections.property("userId"), "userId");
        // prjection.add(Projections.property("title"), "title");
        // prjection.add(Projections.property("description"), "description");
        // prjection.add(Projections.property("createTime"), "createTime");
        // prjection.add(Projections.property("publicTime"), "publicTime");
        // cr.setProjection(prjection);
        // // Set Transformer to convert Object to Post class
        // cr.setResultTransformer(Transformers.aliasToBean(Post.class));

        return (List<Post>) cr.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Post> top()
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
        // Set number posts return and possion
        listQuery.setFirstResult(0);
        listQuery.setMaxResults(Contants.NUMBER_POST_OF_TOP_POST);

        return listQuery.list();
    }
}
