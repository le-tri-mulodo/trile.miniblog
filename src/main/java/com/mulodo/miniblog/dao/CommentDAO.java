/**
 * 
 */
package com.mulodo.miniblog.dao;

import java.util.List;

import com.mulodo.miniblog.pojo.Comment;

/**
 * DAO interface about Comment function
 * 
 * @author TriLe
 */
public interface CommentDAO extends CommonDAO<Comment>
{
    /**
     * @param postId
     *            Id of Post
     * @return All comments of post order by create time ascending
     */
    List<Comment> getByPostId(int postId);
}
