package org.example;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdasUse {
    public static void main(String[] args) {

        List<Integer> ints = new ArrayList<>();
        Random rnd = new Random();

        for (int i = 0; i < 10; i++) {
            ints.add(rnd.nextInt(101));
        }

        System.out.println(ints);

        // -----> 1st: sort even, then: sort odd values
        // using comparator: (T, T) -> int -1/0/+1
        Comparator<Integer> customCompare = (x, y) -> {
            if (x % 2 == 0 && y % 2 != 0) { // as all odds must be later than evens
                return -1;
            } else if (x % 2 != 0 && y % 2 == 0) {
                return 1;
            }
            return x - y; // here both x, y either even or both odd
        };

        // ints.sort(customCompare);

        // ints.sort((x, y) -> compareIntegers(x,y));
        ints.sort(LambdasUse::compareIntegers); // method reference (ссылка на метод)
        System.out.println(ints);

        //----> predicate which compares any string to "java"
        Predicate<String> compareToJava = "java"::equals; // str -> "java".equals(str);
        System.out.println(compareToJava.test("java"));

        // -----> loops naming !!!
        root:
        for (int i = 0; i < 10; i++) {
            child:
            for (int j = 0; j < 10; j++) {
                System.out.println( i + "/" + j);
                break root;
            }
        }

        // ------> reference to constructor
        Supplier<ArrayList<Integer>> arrListSupplierInt = ArrayList::new; // new ArrayList<>();
        IntFunction<ArrayList<String>> arrListSupplierString = ArrayList::new; // new ArrayList<>(int capacity);

        ArrayList<Integer> list1 = arrListSupplierInt.get();
        ArrayList<String> list2 = arrListSupplierString.apply(20);
    }

    private static int compareIntegers(int x, int y){
        if (x % 2 == 0 && y % 2 != 0) { // as all odds must be later than evens
            return -1;
        } else if (x % 2 != 0 && y % 2 == 0) {
            return 1;
        }
        return x - y; // here both x, y either even or both odd
    }

}
