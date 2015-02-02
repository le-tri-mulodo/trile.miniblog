/**
 * 
 */
package com.mulodo.miniblog.provider;

import java.text.SimpleDateFormat;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.stereotype.Component;

import com.mulodo.miniblog.common.Contants;

/**
 * @author TriLe
 *
 */
@Provider
@Component
@Produces(MediaType.APPLICATION_JSON)
public class JacksonConfig implements ContextResolver<ObjectMapper> {
    private final ObjectMapper objectMapper;

    public JacksonConfig() throws Exception {
	this.objectMapper = new ObjectMapper();
	this.objectMapper.setDateFormat(new SimpleDateFormat(Contants.DATE_FULL_FORMAT));
	this.objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> objectType) {
	return objectMapper;
    }
}
