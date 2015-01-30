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
	String format = "%%%s%%";

	System.out.println(String.format(format, "assa"));
	test();
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
