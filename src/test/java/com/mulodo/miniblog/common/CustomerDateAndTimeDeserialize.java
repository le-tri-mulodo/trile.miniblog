package com.mulodo.miniblog.common;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class CustomerDateAndTimeDeserialize extends JsonDeserializer<Date> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public Date deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
	    throws IOException, JsonProcessingException {
	String str = paramJsonParser.getText().trim();
	try {
	    return dateFormat.parse(str);
	} catch (ParseException e) {
	    System.out.println(":(");
	}
	return paramDeserializationContext.parseDate(str);
    }
}
