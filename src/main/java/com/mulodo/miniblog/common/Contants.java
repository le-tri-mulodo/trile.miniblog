/**
 * 
 */
package com.mulodo.miniblog.common;

/**
 * @author TriLe
 */
public class Contants
{

    public static final String HASH_ALGORITHM = "SHA-256";
    public static final String DATE_TIME_FULL_FORMAT = "HH:mm a yyyy/MM/dd";
    public static final String DATE_FULL_FORMAT = "yyyy/MM/dd";

    // 10 days
    public static final long EXPIRED_TIME_ADDTION_MS = 1000 * 60 * 60 * 24 * 10;

    public static final String WORDS_VALID_REGEX = "[a-zA-Z]\\w+";

    public static final String URL_ADD = "/";
    public static final String URL_UPDATE = "/";
    // public static final String URL_DELETE = "/{post_id}/{user_id}/{token}";
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
    public static final String URL_GET_BY_POST = "/posts/{post_id:[0-9]+}";


    public static final String URL_COMMENT = "/comments";

    public static final String MSG_DB_ERR = "Database access error";
    public static final String FOR_DB_ERR = "Database error: %s";
    public static final String MSG_TOKEN_ERR = "Token in request invaild or expired";
    public static final String FOR_TOKEN_ERR = "Token [%s] invaild or expired";
    public static final String MSG_USER_PWD_INVALID = "Login with username or password invalid";
    public static final String MSG_USER_PWD_INVALID_DTL = "User id or password invalid";
    public static final String MSG_USER_NOT_EXIST = "User ID in request does not exist";
    public static final String FOR_USER_NOT_EXIST = "User with id=%d does not exist";
    public static final String MSG_PWD_INVALID = "User id or password invalid";
    public static final String MSG_USER_EXIST = "Username existed";
    public static final String MSG_MISS_ALL_FIELDS = "Miss all fields";
    public static final String MSG_MISS_ALL_FIELDS_DTL = "Must have least one field to update";
    public static final String MSG_FORBIDDEN = "Forbidden. User is not owner of resource";
    public static final String FOR_FORBIDDEN_POST = "User with id=%d is not owner of post with id=%d";
    public static final String MSG_POST_NOT_EXIST = "Post does not exist";
    public static final String FOR_POST_NOT_EXIST = "Post with id= %d does not exist";
    public static final String MSG_COMMENT_NOT_EXIST = "Comment does not exist";
    public static final String FOR_COMMENT_NOT_EXIST = "Comment with id= %d does not exist";
    public static final String FOR_FORBIDDEN_COMMENT = "User with id=%d is not owner of comment with id=%d";

    public static final String MSG_CREATE_USER_SCC = "Create user success!";
    public static final String MSG_UPDATE_USER_SCC = "Account updated success!";
    public static final String FOR_UPDATE_USER_SCC = "Search success! %d results";
    public static final String MSG_GET_USER_SCC = "Get user info success!";
    public static final String MSG_CHANGE_PWD_SCC = "Change password success!";
    public static final String MSG_LOGIN_SCC = "Login success!";
    public static final String MSG_LOGOUT_SCC = "Logout success!";
    public static final String MSG_CREATE_POST_SCC = "Create post success!";
    public static final String MSG_UPDATE_POST_SCC = "Update post success!";
    public static final String MSG_ACT_DEACT_SCC = "Active/Deactive post success!";
    public static final String FOR_GET_ALL_POST_SCC = "Get all posts success! %d results";
    public static final String FOR_GET_ALL_COMMENT_SCC = "Get all comments success! %d results";
    public static final String MSG_DELETE_POST_SCC = "Delete post success!";
    public static final String MSG_CREATE_COMMENT_SCC = "Create comment success!";
    public static final String MSG_UPDATE_COMMENT_SCC = "Update comment success!";
    public static final String MSG_DELETE_COMMENT_SCC = "Delete comment success!";


    public static final int CODE_INPUT_ERR = 1;
    public static final int CODE_OK = 200;
    public static final int CODE_CREATED = 201;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_INTERNAL_ERR = 500;
    public static final int CODE_TOKEN_ERR = 1001;
    public static final int CODE_PWD_INVALID = 1002;
    public static final int CODE_USER_NOT_EXIST = 2001;
    public static final int CODE_POST_NOT_EXIST = 2501;
    public static final int CODE_COMMET_NOT_EXIST = 3001;
    public static final int CODE_DB_ERR = 9001;

}
