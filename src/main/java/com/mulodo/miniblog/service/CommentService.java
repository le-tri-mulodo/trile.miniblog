package com.mulodo.miniblog.service;

import java.util.List;

import com.mulodo.miniblog.pojo.Comment;

/**
 * All services of comment
 * 
 * @author TriLe
 */
public interface CommentService extends CommonService<Comment>
{
    /**
     * @return List of all comments of post in Db and order by create time
     *         ascending
     */
    List<Comment> getByPostId(int postId);

    /**
     * @return List of all comments of user in Db and order by create time
     *         ascending
     */
    List<Comment> getByUserId(int userId);
}
