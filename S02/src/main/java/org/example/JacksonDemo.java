package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.Objects;

public class JacksonDemo {
    public static void main(String[] args) throws IOException {

        Person person = new Person("Boris", 25, 10_000, new Department("none"));

        ObjectMapper objectMapper = new ObjectMapper();

        // makes JSON from class instance
        ObjectWriter writer = objectMapper.writer();
        String str = writer.withDefaultPrettyPrinter().writeValueAsString(person);

        System.out.println(str);

        // makes class instance from JSON
        ObjectReader reader = objectMapper.reader();
        Person person1 = reader.readValue("\n{" +
                "\"name\" : \"Boris\",\n" +
                "\"age\" : 25,\n" +
                "\"salary\" : 10000.0,\n" +
                "\"department\" : {\n" +
                "\"name\" : \"none\"\n" +
                "}\n" +
                "}", Person.class);

        System.out.println(person1);

    }

    static class Person {
        private String name;
        private int age;
        private double salary;
        private Department department;

        public Person() {
        }

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

        public Department() {
        }

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
