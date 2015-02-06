/**
 * 
 */
package com.mulodo.miniblog.common;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

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
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	Date d = null;
	try {
	    d = dateFormat.parse("2014/04/03");
	} catch (ParseException e) {
	    System.out.println(":(");
	}

	System.out.println(d);

	//
	//
	// ObjectMapper mapper = new
	// ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
	// mapper.setDateFormat(new
	// SimpleDateFormat(Contants.DATE_FULL_FORMAT));
	// mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,
	// false);
	// mapper.setSerializationInclusion(Inclusion.NON_NULL);
	//
	// String jsonStr = new String(
	// Files.readAllBytes(Paths
	// .get("/Users/TriLe/Documents/workspace-sts/trile.miniblog/src/main/java/com/mulodo/miniblog/common/json.txt")));
	// // System.out.println(jsonStr);
	//
	// ResultMessage<ArrayList<User>> r = mapper
	// .readValue(
	// Files.readAllBytes(Paths
	// .get("/Users/TriLe/Documents/workspace-sts/trile.miniblog/src/main/java/com/mulodo/miniblog/common/json.txt")),
	// new TypeReference<ResultMessage<ArrayList<User>>>() {
	// });
	//
	// // ResultMessage<ArrayList<User>> r = fromJSON(new
	// // TypeReference<ResultMessage<ArrayList<User>>>() {
	// // }, jsonStr);
	//
	// for (Object obj : r.getData()) {
	// User u = (User) obj;
	// System.out.println(u.getFirstName());
	// }
    }

    public static <T> T fromJSON(final TypeReference<T> type, final String jsonPacket) {
	T data = null;

	try {
	    data = new ObjectMapper().readValue(jsonPacket, type);
	} catch (Exception e) {
	    // Handle the problem
	}
	return data;
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
