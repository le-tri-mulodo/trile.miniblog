package com.mulodo.miniblog.pojo;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.mulodo.miniblog.config.CustomerTimestampDeserialize;

@Entity
@Table(name = "posts")
@JsonPropertyOrder({ "user_id", "post_id", "title", "description", "create_time", "edit_time",
        "public_time" })
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("post_id")
    private int id;

    @Column(name = "title", length = 128, nullable = false)
    @JsonProperty("title")
    private String title;

    @Column(name = "description", length = 128, nullable = false)
    @JsonProperty("description")
    private String description;

    @Column(name = "content", length = 8192, columnDefinition = "TINYTEXT", nullable = false)
    @JsonProperty("content")
    private String content;

    @Column(name = "create_time", columnDefinition = "TIMESTAMP", nullable = false)
    @JsonProperty("create_time")
    @JsonDeserialize(using = CustomerTimestampDeserialize.class)
    private Timestamp createTime;

    @Column(name = "edit_time", columnDefinition = "TIMESTAMP", nullable = true)
    @JsonProperty("edit_time")
    @JsonDeserialize(using = CustomerTimestampDeserialize.class)
    private Timestamp editTime;

    @Column(name = "public_time", columnDefinition = "TIMESTAMP", nullable = true)
    @JsonProperty("public_time")
    @JsonDeserialize(using = CustomerTimestampDeserialize.class)
    private Timestamp publicTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @Cascade(CascadeType.SAVE_UPDATE)
    @ForeignKey(name = "fk_posts_users")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "post", targetEntity = Comment.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Comment> comments;

    @Transient
    @JsonProperty("user_id")
    private int userId;

    public Post() {
        // Current time
        this.createTime = new Timestamp(System.currentTimeMillis());
    }

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

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Post other = (Post) obj;
        if (content == null) {
            if (other.content != null) {
                System.out.println("content");
                return false;
            }
        } else if (!content.equals(other.content)) {
            System.out.println("content");
            return false;
        }
        if (createTime == null) {
            if (other.createTime != null) {
                System.out.println("createTime");
                return false;
            }
        } else if (!timestampEqualIgnoreSecord(createTime, other.createTime)) {
            System.out.println("createTime");
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                System.out.println("description");
                return false;
            }
        } else if (!description.equals(other.description)) {
            System.out.println("description");
            return false;
        }
        if (editTime == null) {
            if (other.editTime != null) {
                System.out.println("editTime");
                return false;
            }
        } else if (!timestampEqualIgnoreSecord(editTime, other.editTime)) {
            System.out.println("editTime");
            return false;
        }
        if (publicTime == null) {
            if (other.publicTime != null) {
                System.out.println("publicTime");
                return false;
            }
        } else if (!timestampEqualIgnoreSecord(publicTime, other.publicTime)) {
            System.out.println("publicTime");
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                System.out.println("title");
                return false;
            }
        } else if (!title.equals(other.title)) {
            System.out.println("title");
            return false;
        }
        if (userId != other.userId) {
            System.out.println("userId");
            return false;
        }
        return true;
    }

    private boolean timestampEqualIgnoreSecord(Timestamp a, Timestamp b) {
        if (null == a && null == b) {
            return true;
        }
        if (null == a || null == b) {
            return false;
        }

        Calendar cal = Calendar.getInstance();
        // A Timestamp
        cal.setTimeInMillis(a.getTime());
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date aDate = cal.getTime();

        // B Timestamp
        cal.setTimeInMillis(b.getTime());
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date bDate = cal.getTime();

        return aDate.equals(bDate);
    }

}
