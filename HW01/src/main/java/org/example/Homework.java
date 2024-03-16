package org.example;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Homework {
    public static void main(String[] args) {

        String[] names = {"Andrey", "Semen", "Petr", "Elena", "Olga", "Serafim", "Victoria", "Pavel",
                "Sergey", "Marina", "Natalia", "Tatiana", "Ivan", "Oleg", "Matvey", "Alina", "Ippolit"};
        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            departments.add(new Department("Department #" + i));
        }

        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            persons.add(new Person(
                    names[ThreadLocalRandom.current().nextInt(names.length)],
                    ThreadLocalRandom.current().nextInt(20, 61),
                    ThreadLocalRandom.current().nextInt(20_000, 100_001) * 1.0,
                    departments.get(ThreadLocalRandom.current().nextInt(departments.size()))
            ));
        }


        // Вывести на консоль отсортированные (по алфавиту) имена персонов
        System.out.println("Task #1");
        printNamesOrdered(persons);

        /**
         * В каждом департаменте найти самого взрослого сотрудника.
         * Вывести на консоль мапипнг department -> personName
         * Map<Department, Person>
         */
        System.out.println("\nTask #2");
        Map<Department, Person> oldestPerson = printDepartmentOldestPerson(persons);
        oldestPerson.forEach((k, v) -> System.out.println(k.getName() + ": " + v.toString()));


        /**
         * Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
         */
        System.out.println("\nTask #3");

        List<Person> first10 = findFirstPersons(persons);
        first10.forEach(System.out::println);


        /**
         * Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
         */
        System.out.println("\nTask #4");

        Optional<Department> bestDept = findTopDepartment(persons);
        bestDept.ifPresent(x -> System.out.println("Самая высокая суммарная зарплата в отделе: " + x.getName()));

        // сумма зарплат по отделам - для проверки
        persons.stream()
                .collect(Collectors.groupingBy(Person::getDepartment,
                        Collectors.summingDouble(Person::getSalary)))
                .forEach((k, v) -> System.out.println(k.getName() + ": " + v));

    }

    /**
     * Реалзиовать методы, описанные ниже:
     */

    /**
     * Вывести на консоль отсортированные (по алфавиту) имена персонов
     */
    public static void printNamesOrdered(List<Person> persons) {
        persons.stream()
                .map(Person::getName)
                .sorted()
                .forEach(System.out::println);
    }

    /**
     * В каждом департаменте найти самого взрослого сотрудника.
     * Вывести на консоль мапипнг department -> personName
     * Map<Department, Person>
     */
    public static Map<Department, Person> printDepartmentOldestPerson(List<Person> persons) {
        Comparator<Person> ageComparator = Comparator.comparing(Person::getAge);

        return persons.stream()
                .collect(Collectors.toMap(Person::getDepartment, x -> x, (person1, person2) -> {
                    if (ageComparator.compare(person1, person2) > 0) {
                        return person1;
                    }
                    return person2;
                }));
    }

    /**
     * Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
     */
    public static List<Person> findFirstPersons(List<Person> persons) {
        return persons.stream()
                .filter(x -> x.getAge() < 30)
                .filter(x -> x.getSalary() > 50_000)
                .limit(10)
                .toList();
    }

    /**
     * Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
     */
    public static Optional<Department> findTopDepartment(List<Person> persons) {

        return persons.stream()
                .collect(Collectors.groupingBy(Person::getDepartment,
                        Collectors.summingDouble(Person::getSalary)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

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