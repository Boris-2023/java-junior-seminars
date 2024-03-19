package org.example.random;

import java.lang.reflect.Field;
import java.util.Random;

public class RandomIntegerInitializer {
    private final static Random rnd = new Random();

    public static void init(Object obj) {

        Class<?> objClass = obj.getClass(); // gets class of the object
        for (Field field : objClass.getDeclaredFields()) { // for all fields in the class
            RandomInteger anno = field.getAnnotation(RandomInteger.class); // if negative -> returns null
            if (anno != null) { // required type of annotation found
                if (int.class.equals(field.getType())) { // if type of the field is 'int'
                    int min = anno.min();
                    int max = anno.max();

                    int rndValue = rnd.nextInt(min, max); // new random value

                    try {
                        field.setAccessible(true); // for private/final - allows to change
                        field.set(obj, rndValue); // set new value to the field
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

}
