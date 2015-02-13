/**
 * 
 */
package com.mulodo.miniblog.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mulodo.miniblog.dao.PostDAO;
import com.mulodo.miniblog.pojo.Post;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.PostService;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 */
@Service
public class PostServiceImpl implements PostService
{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PostDAO postDAO;
    @Autowired
    private UserService userSer;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Post add(Post post)
    {
        // Get user of post
        User user = null;

        // If setted user then not get from Db
        // Else setted userId then get from Db
        if (null == post.getUser()) {
            user = userSer.get(post.getUserId());
            // Set referent to create Fk
            post.setUser(user);
        }

        // Add to Db and return
        return postDAO.add(post);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Post update(Post entity)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(Post entity)
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Post load(int id)
    {
        return postDAO.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Post get(int id)
    {
        return postDAO.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteAll()
    {
        postDAO.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Post> search(String query)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Post activeDeactive(int post_id, boolean activeFlg)
    {
        // Get post
        Post post = get(post_id);

        if (null == post) {
            logger.warn("Post with id ={} does not exist", post_id);
            return null;
        }

        // If activeFlg = TRUE and post not publicized then change Db
        if (activeFlg && null == post.getPublicTime()) {
            post.setPublicTime(new Timestamp(System.currentTimeMillis()));

            // Update Db
            post = postDAO.update(post);

            // If activeFlg = FALSE and post publicized then change
        } else if (!activeFlg && null != post.getPublicTime()) {
            post.setPublicTime(null);

            // Update Db
            post = postDAO.update(post);
        }

        return post;
    }

}
