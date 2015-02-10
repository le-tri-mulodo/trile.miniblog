package com.mulodo.miniblog.config;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class SpringExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(SpringExceptionHandler.class);

    @ExceptionHandler(HibernateException.class)
    public void handleHibernateException(HibernateException exc) {
        logger.error("Have a error: {}", exc);
    }

}
