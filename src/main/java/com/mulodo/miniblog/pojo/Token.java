package com.mulodo.miniblog.pojo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.mulodo.miniblog.common.Contants;
import com.mulodo.miniblog.common.Util;
import com.mulodo.miniblog.config.CustomerTimestampDeserialize;

@Entity
@Table(name = "tokens")
@JsonPropertyOrder({ "user_id", "username", "token", "create_time", "expired_time" })
public class Token
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @Column(name = "create_time", columnDefinition = "TIMESTAMP", nullable = false)
    @JsonProperty("create_time")
    @JsonDeserialize(using = CustomerTimestampDeserialize.class)
    private Timestamp createTime;

    @Column(name = "expired_time", columnDefinition = "TIMESTAMP", nullable = false)
    @JsonProperty("expired_time")
    @JsonDeserialize(using = CustomerTimestampDeserialize.class)
    private Timestamp expiredTime;

    // use SHA-256 to hash (username + id + current time)
    @Column(name = "value", length = 64, nullable = false)
    @JsonProperty("token")
    private String value;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @Cascade(CascadeType.SAVE_UPDATE)
    @ForeignKey(name = "fk_tokens_users")
    @JsonIgnore
    private User user;

    @JsonProperty("username")
    @Transient
    private String userName;

    @JsonProperty("user_id")
    // @Transient
    @Column(name = "user_id", updatable = false, insertable = false)
    private int userid;

    /**
     * 
     */
    public Token() {
        this(null);
    }

    /**
     * 
     */
    public Token(User user) {
        createTime = new Timestamp(System.currentTimeMillis());
        // Create expired time
        expiredTime = new Timestamp(System.currentTimeMillis() + Contants.EXPIRED_TIME_ADDTION_MS);

        if (null != user) {
            // Create new token from userId
            this.value = Util.createToken(user.getId());
            this.user = user;
            this.userid = user.getId();
        }
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
     * @return the expiredTime
     */
    public Timestamp getExpiredTime()
    {
        return expiredTime;
    }

    /**
     * @param expiredTime
     *            the expiredTime to set
     */
    public void setExpiredTime(Timestamp expiredTime)
    {
        this.expiredTime = expiredTime;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
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
        if (null != user) {
            // Create new token from userId
            this.value = Util.createToken(user.getId());
            this.user = user;
            this.userid = user.getId();
        }
    }

    /**
     * @return the userName
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * @return the userid
     */
    public int getUserid()
    {
        return userid;
    }

    /**
     * @param userid
     *            the userid to set
     */
    public void setUserid(int userid)
    {
        this.userid = userid;
    }
}
