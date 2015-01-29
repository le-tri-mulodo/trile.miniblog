/**
 * 
 */
package com.mulodo.miniblog.pojo;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * @author TriLe
 *
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", length = 64, nullable = false)
    private String userName;

    @Column(name = "first_name", length = 64, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 64, nullable = false)
    private String lastName;

    // use SHA-256 to hash pass
    @Column(name = "pass_hash", length = 64, nullable = false)
    private String passHash;

    @Column(name = "avatar_link", length = 256, nullable = true)
    private String avatarLink;

    @Column(name = "join_date", columnDefinition = "DATE", nullable = false)
    private Date joinDate;

    // Origin password
    // private String passWord

    @OneToMany(mappedBy = "user", targetEntity = Post.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    private Set<Post> posts;

    @OneToMany(mappedBy = "user", targetEntity = Comment.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "user", targetEntity = Token.class)
    @Cascade(CascadeType.SAVE_UPDATE)
    private Set<Token> tokens;

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

    // /**
    // * @return the passWord
    // */
    // public String getPassWord() {
    // return passWord;
    // }

    /**
     * @param passWord
     *            the passWord to set
     */
    public void setPassWord(String passWord) {
	// Reset hash password
	passHash = hashSHA(passWord);
    }

    public boolean isMatchPass(String org) {
	// TODO: Add hash compare
	return true;
    }

    private String hashSHA(String ip) {
	return ip;
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
}
