package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class StreamsIntro {
    public static void main(String[] args) {

        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            ints.add(ThreadLocalRandom.current().nextInt(101));
        }
        System.out.println(ints);

        // All values < 50 - multiply by 2 and print into console
        ints.stream()
                .parallel()
                .filter(x -> x < 50)
                .map(x -> x * 2 + " ")
                .sequential()
                .forEach(System.out::print);

        // makes a stream with randoms, takes 5 first elements and save them into list
        Stream.generate(() -> ThreadLocalRandom.current().nextInt(101))
                .limit(5)
                .toList();

        // iterate
        System.out.println();
        Stream.iterate(0, x -> x+1)
                .limit(10)
                .map(x -> x + " ")
                .forEach(System.out::print);

    }
}