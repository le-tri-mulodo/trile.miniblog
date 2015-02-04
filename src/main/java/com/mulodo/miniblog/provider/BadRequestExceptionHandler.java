/**
 * 
 */
package com.mulodo.miniblog.provider;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.BadRequestException;
import org.springframework.stereotype.Component;

import com.mulodo.miniblog.message.ResultMessage;

/**
 * @author TriLe
 *
 */
@Provider
@Component
public class BadRequestExceptionHandler implements ExceptionMapper<BadRequestException> {

    @SuppressWarnings("rawtypes")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(BadRequestException exception) {
	ResultMessage resultMsg = new ResultMessage(1, "Input validation failed", exception.getMessage());

	return Response.status(Status.BAD_REQUEST).entity(resultMsg).build();
    }
}
