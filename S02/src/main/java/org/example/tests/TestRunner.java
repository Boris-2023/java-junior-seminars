package org.example.tests;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class TestRunner {

    public static void run(Class<?> testClass) {
        final Object testObject = initTestObject(testClass);

        for (Method method : testClass.getDeclaredMethods()) {

            if (method.getModifiers() == Modifier.PRIVATE) {
                continue;
            }

            if (method.getAnnotation(Test.class) != null) {
                try {
                    method.invoke(testObject);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

    private static Object initTestObject(Class<?> testClass) {
        try {
            Constructor<?> noArgsConstructor = testClass.getConstructor();
            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("There is no by-default constructor!");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create an instance of test class!");
        }
    }


}
