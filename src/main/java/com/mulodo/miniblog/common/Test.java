/**
 * 
 */
package com.mulodo.miniblog.common;

/**
 * @author TriLe
 *
 */
public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
	try {
	    ReflectionHelpper.getParamNameFromPath("as");
	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (NoSuchMethodException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SecurityException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
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
