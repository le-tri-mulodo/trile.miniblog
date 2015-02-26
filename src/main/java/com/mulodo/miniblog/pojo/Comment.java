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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.mulodo.miniblog.config.CustomerTimestampDeserialize;

/**
 * @author TriLe
 */
@Entity
@Table(name = "comments")
@JsonPropertyOrder({ "user_id", "post_id", "comment_id", "title", "description", "create_time",
        "edit_time" })
public class Comment
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("comment_id")
    private int id;

    @Column(name = "content", length = 256, nullable = false)
    private String content;

    @Column(name = "create_time", columnDefinition = "TIMESTAMP", nullable = false)
    @JsonDeserialize(using = CustomerTimestampDeserialize.class)
    @JsonProperty("create_time")
    private Timestamp createTime;

    @Column(name = "edit_time", columnDefinition = "TIMESTAMP", nullable = true)
    @JsonDeserialize(using = CustomerTimestampDeserialize.class)
    @JsonProperty("edit_time")
    private Timestamp editTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @Cascade(CascadeType.SAVE_UPDATE)
    @ForeignKey(name = "fk_comments_users")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @ForeignKey(name = "fk_comments_posts")
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = true)
    @ForeignKey(name = "fk_comments_comments")
    @JsonIgnore
    private Comment parent;

    @OneToMany(mappedBy = "parent", targetEntity = Comment.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Comment> comments;

    @Column(name = "user_id", updatable = false, insertable = false)
    @JsonProperty("user_id")
    private int userId;

    @Column(name = "post_id", updatable = false, insertable = false)
    @JsonProperty("post_id")
    private int postId;

    @Column(name = "comment_id", updatable = false, insertable = false)
    @JsonProperty("pcomment_id")
    private Integer commentId;

    /**
     * 
     */
    public Comment() {
        // Set current date time to create time
        createTime = new Timestamp(System.currentTimeMillis());
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the content
     */
    public String getContent()
    {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content)
    {
        this.content = content;
    }

    /**
     * @return the createTime
     */
    public Timestamp getCreateTime()
    {
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    /**
     * @return the editTime
     */
    public Timestamp getEditTime()
    {
        return editTime;
    }

    /**
     * @param editTime
     *            the editTime to set
     */
    public void setEditTime(Timestamp editTime)
    {
        this.editTime = editTime;
    }

    /**
     * @return the user
     */
    public User getUser()
    {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * @return the post
     */
    public Post getPost()
    {
        return post;
    }

    /**
     * @param post
     *            the post to set
     */
    public void setPost(Post post)
    {
        this.post = post;
    }

    /**
     * @return the parent
     */
    public Comment getParent()
    {
        return parent;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(Comment parent)
    {
        this.parent = parent;
    }

    /**
     * @return the comments
     */
    public Set<Comment> getComments()
    {
        return comments;
    }

    /**
     * @param comments
     *            the comments to set
     */
    public void setComments(Set<Comment> comments)
    {
        this.comments = comments;
    }

    /**
     * @return the userId
     */
    public int getUserId()
    {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    /**
     * @return the postId
     */
    public int getPostId()
    {
        return postId;
    }

    /**
     * @param postId
     *            the postId to set
     */
    public void setPostId(int postId)
    {
        this.postId = postId;
    }

    /**
     * @return the commentId
     */
    public Integer getCommentId()
    {
        return commentId;
    }

    /**
     * @param commentId
     *            the commentId to set
     */
    public void setCommentId(Integer commentId)
    {
        this.commentId = commentId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Comment other = (Comment) obj;
        if (null == commentId) {
            if (null != other.commentId) {
                //System.out.println("commentId");
                return false;
            }
        } else if (!commentId.equals(other.commentId)) {
            //System.out.println("commentId");
            return false;
        }
        if (null == content) {
            if (null != other.content) {
                //System.out.println("content");
                return false;
            }
        } else if (!content.equals(other.content)) {
            //System.out.println("content");
            return false;
        }
        if (postId != other.postId) {
            //System.out.println("postId " + postId + " " + other.postId);
            return false;
        }
        if (userId != other.userId) {
            //System.out.println("userId");
            return false;
        }
        return true;
    }

}
