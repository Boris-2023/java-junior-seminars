package org.example;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamsUse {
    public static void main(String[] args) {

        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            departments.add(new Department("Department #" + i));
        }

        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            persons.add(new Person(
                    "Person #" + i,
                    ThreadLocalRandom.current().nextInt(20, 61),
                    ThreadLocalRandom.current().nextInt(20_000, 100_001) * 1.0,
                    departments.get(ThreadLocalRandom.current().nextInt(departments.size()))
            ));
        }

        // Find a person who earns most of all
        persons.stream()
                .max(Comparator.comparing(Person::getSalary))
                .ifPresent(System.out::println);

        // Find person who are older than 40 y.o and works in dept # >3, save into LinkedList
        System.out.println();
        Function<Person, Integer> deptNumberExtractor = person -> {
            String dept = person.getDepartment().getName();
            return Integer.parseInt(dept.split("#")[1]);
        };
        LinkedList<Person> collect = persons.stream()
                .filter(x -> x.getAge() > 40)
                //.filter(x -> Integer.parseInt(x.getDepartment().getName().split("#")[1]) > 3)
                .filter(x -> deptNumberExtractor.apply(x) > 3)
                .collect(Collectors.toCollection(LinkedList::new));
        System.out.println(collect);

        // Find depts where there are persons who earn more than average
        System.out.println();
        double averageSalary = persons.stream()
                .mapToDouble(Person::getSalary)
                .average()
                .orElse(0.0);

        persons.stream()
                .filter(x -> x.getSalary() > averageSalary)
                .map(x -> x.getDepartment().getName())
                .distinct()
                .sorted()
                .forEach(System.out::println);

        // make Map <String, List<Person>> - key = name Dept, value = persons who work in this dept
        Map<String, List<Person>> personsByDept = persons.stream()
                .collect(Collectors.groupingBy(it -> it.getDepartment().getName()));

        // make Map <String, Person> - key = name Dept, value = person who earns the most in the Dept
        Comparator<Person> salaryComparator = Comparator.comparing(Person::getSalary);
        Map<String, Person> maxSalary = persons.stream()
                .collect(Collectors.toMap(x -> "\n" + x.getDepartment().getName(), x -> x, (person1, person2) -> {
                    if (salaryComparator.compare(person1, person2) > 0) {
                        return person1;
                    }
                    return person2;
                }));

        System.out.println(maxSalary);


        // Print uppercase symbols from a string given
        String str = "dfdfFSDFfsDfsDFSDFsfsdFSFsdfdfRYe";

        String upper = str.chars() // IntStream
                .filter(Character::isUpperCase) // IntStream
                .mapToObj(x -> (char) x) // types: int -> char -> Character
                .map(String::valueOf) // transform to String
                .collect(Collectors.joining("")); // makes one string of many

        System.out.println(upper);
    }

    static class Person {
        private String name;
        private int age;
        private double salary;
        private Department department;

        public Person(String name, int age, double salary, Department department) {
            this.name = name;
            this.age = age;
            this.salary = salary;
            this.department = department;
        }

        @Override
        public String toString() {
            return "Person {" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", salary=" + salary +
                    ", department=" + department.getName() +
                    '}';
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public double getSalary() {
            return salary;
        }

        public Department getDepartment() {
            return department;
        }
    }

    static class Department {
        private String name;

        public Department(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Department that = (Department) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        public String getName() {
            return name;
        }
    }
}
