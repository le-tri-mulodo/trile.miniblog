/**
 * 
 */
package com.mulodo.miniblog.pojo;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

/**
 * @author TriLe
 *
 */
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content", length = 256, nullable = false)
    private String content;

    @Column(name = "create_time", columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp createTime;

    @Column(name = "edit_time", columnDefinition = "TIMESTAMP", nullable = true)
    private Timestamp editTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @Cascade(CascadeType.SAVE_UPDATE)
    @ForeignKey(name = "fk_comments_users")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @ForeignKey(name = "fk_comments_posts")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false)
    @ForeignKey(name = "fk_comments_comments")
    private Comment parent;

    @OneToMany(mappedBy = "parent", targetEntity = Comment.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    private Set<Comment> comments;

    /**
     * @return the id
     */
    public int getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
	this.id = id;
    }

    /**
     * @return the content
     */
    public String getContent() {
	return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
	this.content = content;
    }

    /**
     * @return the createTime
     */
    public Timestamp getCreateTime() {
	return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Timestamp createTime) {
	this.createTime = createTime;
    }

    /**
     * @return the editTime
     */
    public Timestamp getEditTime() {
	return editTime;
    }

    /**
     * @param editTime
     *            the editTime to set
     */
    public void setEditTime(Timestamp editTime) {
	this.editTime = editTime;
    }

    /**
     * @return the user
     */
    public User getUser() {
	return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(User user) {
	this.user = user;
    }

    /**
     * @return the post
     */
    public Post getPost() {
	return post;
    }

    /**
     * @param post
     *            the post to set
     */
    public void setPost(Post post) {
	this.post = post;
    }

    /**
     * @return the parent
     */
    public Comment getParent() {
	return parent;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(Comment parent) {
	this.parent = parent;
    }

    /**
     * @return the comments
     */
    public Set<Comment> getComments() {
	return comments;
    }

    /**
     * @param comments
     *            the comments to set
     */
    public void setComments(Set<Comment> comments) {
	this.comments = comments;
    }

}
