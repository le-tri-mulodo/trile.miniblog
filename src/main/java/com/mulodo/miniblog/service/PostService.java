/**
 * 
 */
package com.mulodo.miniblog.service;

import java.util.List;

import com.mulodo.miniblog.pojo.Post;

/**
 * @author TriLe
 */
public interface PostService extends CommonService<Post>
{

    /**
     * Search in title, description and content contain query string
     * 
     * @param query
     *            Query to search title, description and content contain in post
     * @return list of posts match with query
     */
    List<Post> search(String query);

    /**
     * Active/Deactive post
     * 
     * @param post_id
     *            Id of post
     * @param activeFlg
     *            <b>TRUE</b> Active post<br>
     *            <b>FALSE</b> Deactive post
     * @return Post info
     */
    Post activeDeactive(int post_id, boolean activeFlg);

}
