/**
 * 
 */
package com.mulodo.miniblog.config;

import java.text.SimpleDateFormat;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.stereotype.Component;

import com.mulodo.miniblog.common.Contants;

/**
 * @author TriLe
 */
@Provider
@Component
public class JacksonConfig implements ContextResolver<ObjectMapper> {
    private final ObjectMapper objectMapper;

    /**
     * This class is a config class which call when Resteasy use Jackson to
     * serialize Object to Json string
     * 
     * @throws Exception
     */
    public JacksonConfig() throws Exception {

        // serialize all fields in object
        this.objectMapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
        // set date time format
        this.objectMapper.setDateFormat(new SimpleDateFormat(Contants.DATE_FULL_FORMAT));
        this.objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        // ignore null fields
        this.objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
    }

    @Override
    public ObjectMapper getContext(Class<?> objectType) {
        return objectMapper;
    }
}
