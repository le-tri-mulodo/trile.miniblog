package com.mulodo.miniblog.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.mulodo.miniblog.common.Contants;

/**
 * Serialize date with custom format defined in
 * {@link com.mulodo.miniblog.common.Contants.DATE_FULL_FORMAT}
 * 
 * @author TriLe
 */
public class CustomerDateSerialize extends JsonSerializer<Date> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat(Contants.DATE_FULL_FORMAT);

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeString(dateFormat.format(value));
    }
}
