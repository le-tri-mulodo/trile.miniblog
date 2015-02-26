/**
 * 
 */
package com.mulodo.miniblog.dao.impl;

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
}
