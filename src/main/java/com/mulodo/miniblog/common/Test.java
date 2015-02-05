/**
 * 
 */
package com.mulodo.miniblog.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.mulodo.miniblog.pojo.User;

/**
 * @author TriLe
 *
 */
public class Test {

    /**
     * @param args
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
	
	List<User> users = new ArrayList<User>();

	User user = new User();
	user.setFirstName("Tri");
	user.setLastName("Le");

	users.add(user);

	user = new User();
	user.setFirstName("Peter");
	user.setLastName("Parker");

	users.add(user);

	ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
	mapper.setDateFormat(new SimpleDateFormat(Contants.DATE_FULL_FORMAT));
	mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
	mapper.setSerializationInclusion(Inclusion.NON_NULL);

	String jsonStr = null;
	    jsonStr = mapper.writeValueAsString(users);
	System.out.println(jsonStr);
    }

    public static void test() {
	int i = 0;
	long prev_time = System.currentTimeMillis();
	long time;

	String s;
	for (i = 0; i < 100000; i++) {
	    s = "Blah" + i + "Blah";
	}
	time = System.currentTimeMillis() - prev_time;

	System.out.println("Time after for loop " + time);

	prev_time = System.currentTimeMillis();
	for (i = 0; i < 100000; i++) {
	    s = String.format("Blah %d Blah", i);
	}
	time = System.currentTimeMillis() - prev_time;
	System.out.println("Time after for loop " + time);

    }
}
