/**
 * 
 */
package com.mulodo.miniblog.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author TriLe
 */
public class ReflectionHelpper {

    public static String getParamNameFromPath(String path) throws ClassNotFoundException,
            NoSuchMethodException, SecurityException {
        Class<?> cls = Class.forName("com.mulodo.miniblog.rest.controller.UserController");
        Method[] ms = cls.getMethods();
        for (Method m : ms) {
            System.out.println("method: " + m.getName());
            Annotation[][] pas = m.getParameterAnnotations();
            for (Annotation[] pa : pas) {
                for (Annotation a : pa) {
                    // System.out.println(a.annotationType().getName().get);
                }
            }
        }
        return "aha";
    }
}
