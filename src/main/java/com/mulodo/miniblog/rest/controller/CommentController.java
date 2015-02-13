/**
 * 
 */
package com.mulodo.miniblog.rest.controller;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.mulodo.miniblog.common.Contants;

/**
 * @author TriLe
 */
@Controller
@Path(Contants.URL_COMMENT)
@Produces(MediaType.APPLICATION_JSON)
public class CommentController
{
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
}
