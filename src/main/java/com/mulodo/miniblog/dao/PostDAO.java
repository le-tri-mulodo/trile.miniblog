/**
 * 
 */
package com.mulodo.miniblog.dao;

import java.util.List;

import com.mulodo.miniblog.pojo.Post;

/**
 * @author TriLe
 */
public interface PostDAO extends CommonDAO<Post>
{
    /**
     * Check user is owner of post
     * 
     * @param postId
     *            Id of post
     * @param userId
     *            Id of user
     * @return <b>TRUE</b> if User with input userId is owner of Post
     */
    boolean checkOwner(int postId, int userId);

    /**
     * @param userId
     *            Id of User
     * @return All public post of user order by create time desc
     */
    List<Post> getByUserId(int userId);
}
