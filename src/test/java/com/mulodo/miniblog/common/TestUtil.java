/**
 * 
 */
package com.mulodo.miniblog.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mulodo.miniblog.service.TokenService;
import com.mulodo.miniblog.service.UserService;

/**
 * @author TriLe
 */
@Component
public class TestUtil
{

    @Autowired
    public TokenService tokenSer;

    @Autowired
    public UserService userSer;

}
