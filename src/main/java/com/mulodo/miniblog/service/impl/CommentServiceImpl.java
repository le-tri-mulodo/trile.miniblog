/**
 * 
 */
package com.mulodo.miniblog.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mulodo.miniblog.dao.CommentDAO;
import com.mulodo.miniblog.pojo.Comment;
import com.mulodo.miniblog.pojo.Post;
import com.mulodo.miniblog.pojo.User;
import com.mulodo.miniblog.service.CommentService;
import com.mulodo.miniblog.service.PostService;
import com.mulodo.miniblog.service.TokenService;
import com.mulodo.miniblog.service.UserService;

import exception.ResourceNotExistException;
import exception.ResourceNotExistException.Resource;

/**
 * All services of comment
 * 
 * @author TriLe
 */
@Service
public class CommentServiceImpl implements CommentService
{

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    private PostService postSer;
    @Autowired
    private UserService userSer;
    @Autowired
    private TokenService tokenSer;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Comment add(Comment comment)
    {
        // Set owner
        // If setted user then not get from Db
        // Else setted userId then get from Db
        if (null == comment.getUser()) {
            if (0 < comment.getUserId()) {
                // Get owner of comment
                User user = userSer.get(comment.getUserId());

                // Check user NOT exist
                if (null == user) {
                    // Throw exception user NOT exist
                    throw new ResourceNotExistException(Resource.USER);
                }
                // Set referent to create Fk
                comment.setUser(user);
            } else {
                throw new IllegalArgumentException("User ID must greater than 0");
            }
        }

        // Set post
        // If setted post then not get from Db
        // Else setted postId then get from Db
        if (null == comment.getPost()) {
            if (0 < comment.getPostId()) {
                // Get owner of comment
                Post post = postSer.get(comment.getPostId());

                // Check post NOT exist
                if (null == post) {
                    // Throw exception post NOT exist
                    throw new ResourceNotExistException(Resource.POST);
                }
                // Set referent to create Fk
                comment.setPost(post);
            } else {
                throw new IllegalArgumentException("Post ID must greater than 0");
            }
        }

        // Set parent comment if exist (CommentId >0)
        // If setted post then not get from Db
        // Else setted postId then get from Db
        if (null == comment.getComments() && null != comment.getCommentId()
                && 0 < comment.getCommentId()) {
            // Get owner of comment
            Comment pComment = commentDAO.get(comment.getCommentId());

            // Check post NOT exist
            if (null == pComment) {
                // Throw exception parent comment NOT exist
                throw new ResourceNotExistException(Resource.COMMENT);
            }
            // Set referent to create Fk
            comment.setParent(pComment);
        }

        return commentDAO.add(comment);
    }

    @Override
    public Comment update(Comment entity)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Comment entity)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public Comment load(int id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Comment get(int id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteAll()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Comment> list()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
