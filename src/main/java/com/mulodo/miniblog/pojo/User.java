/**
 * 
 */
package com.mulodo.miniblog.pojo;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.mulodo.miniblog.common.Util;
import com.mulodo.miniblog.config.CustomerDateDeserialize;
import com.mulodo.miniblog.config.CustomerDateSerialize;

/**
 * @author TriLe
 */
@Entity
@Table(name = "users")
@JsonPropertyOrder({ "user_id", "username", "firstname", "lastname", "joindate", "avatarlink",
        "token" })
// @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User implements Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("user_id")
    private int id;

    @Column(name = "username", length = 64, nullable = false, unique = true)
    @JsonProperty("username")
    private String userName;

    @Column(name = "first_name", length = 64, nullable = false)
    @JsonProperty("firstname")
    private String firstName;

    @Column(name = "last_name", length = 64, nullable = false)
    @JsonProperty("lastname")
    private String lastName;

    // use SHA-256 to hash pass
    @Column(name = "pass_hash", length = 64, nullable = false)
    @JsonIgnore
    private String passHash;

    @Column(name = "avatar_link", length = 256, nullable = true)
    @JsonProperty("avatarlink")
    private String avatarLink;

    @Column(name = "join_date", columnDefinition = "DATE", nullable = false)
    @JsonProperty("joindate")
    @JsonDeserialize(using = CustomerDateDeserialize.class)
    @JsonSerialize(using = CustomerDateSerialize.class)
    private Date joinDate;

    // Origin password
    // private String passWord

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", targetEntity =
    // Post.class)
    @OneToMany(mappedBy = "user", targetEntity = Post.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Post> posts;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", targetEntity =
    // Comment.class)
    @OneToMany(mappedBy = "user", targetEntity = Comment.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Comment> comments;

    // @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", targetEntity =
    // Token.class)
    @OneToMany(mappedBy = "user", targetEntity = Token.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JsonIgnore
    private Set<Token> tokens;

    @Transient
    @JsonProperty("token")
    private String token;

    /**
     * 
     */
    public User() {
        // Create Date ignore time
        this.joinDate = Util.createDateIgnoreTime();
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
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // /**
    // * @return the passHash
    // */
    // public String getPassHash() {
    // return passHash;
    // }

    /**
     * @param passHash
     *            the passHash to set
     */
    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    /**
     * @return the avatarLink
     */
    public String getAvatarLink() {
        return avatarLink;
    }

    /**
     * @param avatarLink
     *            the avatarLink to set
     */
    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    /**
     * @return the passHash
     */
    public String getPassHash() {
        return passHash;
    }

    /**
     * @return the joinDate
     */
    public Date getJoinDate() {
        return joinDate;
    }

    /**
     * @param joinDate
     *            the joinDate to set
     */
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    /**
     * @return the posts
     */
    public Set<Post> getPosts() {
        return posts;
    }

    /**
     * @param posts
     *            the posts to set
     */
    public void setPosts(Set<Post> posts) {
        this.posts = posts;
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
     * @return the tokens
     */
    public Set<Token> getTokens() {
        return tokens;
    }

    /**
     * @param tokens
     *            the tokens to set
     */
    public void setTokens(Set<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int compareTo(User o) {
        return id - o.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null) {
            System.out.println("null");
            return false;
        }
        if (getClass() != obj.getClass()) {
            System.out.println("class");
            return false;
        }
        User other = (User) obj;
        if (avatarLink == null) {
            if (other.avatarLink != null) {
                System.out.println("avatarLink");
                return false;
            }
        } else if (!avatarLink.equals(other.avatarLink)) {
            System.out.println("avatarLink");
            return false;
        }
        if (firstName == null) {
            if (other.firstName != null)
                System.out.println("firstName");
            return false;
        } else if (!firstName.equals(other.firstName)) {
            System.out.println("firstName");
            return false;
        }
        if (joinDate == null) {
            if (other.joinDate != null) {
                System.out.println("joinDate");
                return false;
            }
        } else if (!joinDate.equals(other.joinDate)) {
            System.out.println("joinDate");
            return false;
        }
        if (lastName == null) {
            if (other.lastName != null) {
                System.out.println("lastName");
                return false;
            }
        } else if (!lastName.equals(other.lastName)) {
            System.out.println("lastName");
            return false;
        }
        if (userName == null) {
            if (other.userName != null) {
                System.out.println("userName");
                return false;
            }
        } else if (!userName.equals(other.userName)) {
            System.out.println("userName");
            return false;
        }
        return true;
    }

}
