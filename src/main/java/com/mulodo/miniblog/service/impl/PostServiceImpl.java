/**
 * 
 */
package com.mulodo.miniblog.service.impl;

import java.util.List;

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
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDAO postDAO;
    @Autowired
    private UserService userSer;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Post add(Post post) {
        // Get user of post
        User user = userSer.get(post.getUserId());
        // Set referent to create Fk
        post.setUser(user);

        // Add to Db and return
        return postDAO.add(post);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Post update(Post entity) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(Post entity) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Post load(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Post get(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteAll() {
        postDAO.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Post> search(String query) {
        // TODO Auto-generated method stub
        return null;
    }

}
