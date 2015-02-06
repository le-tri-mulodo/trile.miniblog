/**
 * 
 */
package com.mulodo.miniblog.config;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.api.validation.ResteasyViolationException;
import org.springframework.stereotype.Component;

import com.mulodo.miniblog.message.ResultMessage;

/**
 * @author TriLe
 *
 */
@Provider
@Component
public class ValidationExceptionHandler implements ExceptionMapper<ResteasyViolationException> {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(ResteasyViolationException exception) {

	List<String> messages = new ArrayList<String>();
	for (ResteasyConstraintViolation violation : exception.getViolations()) {
	    messages.add(violation.getMessage());
	}
	ResultMessage resultMsg = new ResultMessage(1, "Input validation failed", messages);

	return Response.status(Status.BAD_REQUEST).entity(resultMsg).build();
    }
}
