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

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", length = 128, nullable = false)
    private String title;

    @Column(name = "description", length = 128, nullable = false)
    private String description;

    @Column(name = "content", length = 8192, columnDefinition = "TINYTEXT", nullable = false)
    private String content;

    @Column(name = "create_time", columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp createTime;

    @Column(name = "edit_time", columnDefinition = "TIMESTAMP", nullable = true)
    private Timestamp editTime;

    @Column(name = "public_time", columnDefinition = "TIMESTAMP", nullable = true)
    private Timestamp publicTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @Cascade(CascadeType.SAVE_UPDATE)
    @ForeignKey(name = "fk_posts_users")
    private User user;

    @OneToMany(mappedBy = "post", targetEntity = Comment.class)
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
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
	this.description = description;
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
     * @return the publicTime
     */
    public Timestamp getPublicTime() {
	return publicTime;
    }

    /**
     * @param publicTime
     *            the publicTime to set
     */
    public void setPublicTime(Timestamp publicTime) {
	this.publicTime = publicTime;
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
