/**
 * 
 */
package com.mulodo.miniblog.common;

/**
 * @author TriLe
 */
public class Contants {

    public static final String HASH_ALGORITHM = "SHA-256";
    public static final String DATE_TIME_FULL_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FULL_FORMAT = "yyyy/MM/dd";

    // 10 days
    public static final long EXPIRED_TIME_ADDTION_MS = 1000 * 60 * 60 * 24 * 10;

    public static final String WORDS_VALID_REGEX = "[a-zA-Z]\\w+";

    public static final String URL_ADD = "/";
    public static final String URL_UPDATE = "/";
    public static final String URL_DELETE = "/";
    public static final String URL_GET = "/";
    public static final String URL_SEARCH = "/search/{query}";
    public static final String URL_GET_BY_ID = "/{id:[0-9]+}";

    public static final String URL_TOKEN = "/tokens";
    public static final String URL_LOGIN = "/login";
    public static final String URL_LOGOUT = "/logout";

    public static final String URL_USER = "/users";
    public static final String URL_CHPWD = "/pass";

    public static final String URL_POST = "/posts";
    public static final String URL_PUBLICT = "/pub";
    public static final String URL_TOP = "/top";
    public static final String URL_GET_BY_USER = "/users/{user_id:[0-9]+}";

    public static final String URL_COMMENT = "/comments";
}
