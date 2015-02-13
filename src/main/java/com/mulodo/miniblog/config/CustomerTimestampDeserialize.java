package com.mulodo.miniblog.config;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mulodo.miniblog.common.Contants;

/**
 * Deserialize timestamp with custom format defined in
 * {@link com.mulodo.miniblog.common.Contants.DATE_TIME_FULL_FORMAT}
 * 
 * @author TriLe
 */
public class CustomerTimestampDeserialize extends JsonDeserializer<Timestamp>
{

    private static final Logger logger = LoggerFactory
            .getLogger(CustomerTimestampDeserialize.class);

    private SimpleDateFormat dateFormat = new SimpleDateFormat(Contants.DATE_TIME_FULL_FORMAT);

    @Override
    public Timestamp deserialize(JsonParser paramJsonParser,
            DeserializationContext paramDeserializationContext) throws IOException,
            JsonProcessingException
    {
        String str = paramJsonParser.getText().trim();
        try {
            Date date = dateFormat.parse(str);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            logger.error("Parse Timestamp error: ", e);
        }
        return null;
    }
}
