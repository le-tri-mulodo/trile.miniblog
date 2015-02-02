/**
 * 
 */
package com.mulodo.miniblog.provider;

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

import com.mulodo.miniblog.message.ErrorMessage;

/**
 * @author TriLe
 *
 */
@Provider
@Component
public class ValidationExceptionHandler implements ExceptionMapper<ResteasyViolationException> {

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(ResteasyViolationException exception) {

	List<String> messages = new ArrayList<String>();
	for (ResteasyConstraintViolation violation : exception.getViolations()) {
	    messages.add(violation.getMessage());
	}
	ErrorMessage errMsg = new ErrorMessage(0, "Input validation failed", messages);

	return Response.status(Status.BAD_REQUEST).entity(errMsg).build();
    }
}
