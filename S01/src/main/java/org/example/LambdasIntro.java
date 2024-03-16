package org.example;

import java.util.Random;
import java.util.function.*;

public class LambdasIntro {
    public static void main(String[] args) {
        // via class implementing Interface Foo
        FooClass fooClass = new FooClass();
        fooClass.foo();

        // via Interface & anonymous class
        Foo foo = new Foo() {
            @Override
            public void foo() {
                System.out.println("Hello from anonymous Foo!");
            }
        };
        foo.foo();
        System.out.println(foo.getClass().getName());

        // via lambda #1
        Foo fooLambda = () -> System.out.println("Hello from Lambda #1");
        fooLambda.foo();

        // Lambda takes integer and returns it squared (interface SquareInterface())
        SquareInterface sq = x -> x * x;
        System.out.println(sq.square(5));

        // via build-in Java interface UnaryOperator
        UnaryOperator<Integer> sqUnaryOperator = x -> x * x;
        System.out.println(sqUnaryOperator.apply(6));

        // takes string, returns its length (int)
        Function<String, Integer> sampleFunction = x -> x.length();
        System.out.println(sampleFunction.apply("Hello!"));

        // takes smth, but return nothing
        Consumer<String> printer = str -> System.out.println(str);
        printer.accept("Hello from Consumer!");

        // consumes nothing, return smth: generating random int values 0 -> 100
//        Supplier<Integer> randomizer = () -> {
//            return new Random().nextInt(101); // 0 -> 100 including edges
//        };
        Supplier<Integer> randomizer = () -> new Random().nextInt(101);
        System.out.println(randomizer.get());

        // takes nothing and return nothing, print e.g.
        Runnable runnable = () -> System.out.println(new Random().nextInt(101));
        runnable.run();

        // checks value to be even TRUE/FALSE, returns primitive boolean!
        Predicate<Integer> evenTester = n -> n % 2 == 0;
        System.out.println(evenTester.test(25));

    }


    interface SquareInterface {
        int square(int value);
    }

    interface Foo {
        void foo();
    }

    static class FooClass implements Foo {
        @Override
        public void foo() {
            System.out.println("Hello from Foo!");
        }
    }
}
