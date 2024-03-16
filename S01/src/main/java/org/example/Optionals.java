package org.example;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Optionals {
    public static void main(String[] args) {
        List<String> langs = List.of("Java", "Goe", "C++", "Pascal", "Python", "Kotlin", "Golang", "Basic");

        Optional<String> first = langs.stream()
                .filter(x -> x.length() < 3)
                .findFirst();

        String lang = first.orElse("No such language");
        System.out.println(lang);

        first.ifPresent(System.out::println);

//        Optional<String> upperCase = first.map(it -> it.toUpperCase());
//        System.out.println(upperCase);


    }
}
