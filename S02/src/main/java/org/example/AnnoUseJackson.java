package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public class AnnoUseJackson {

    public static void main(String[] args) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        // makes JSON from class instance
        ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();

        Student student = new Student();
        student.setFirstName("Boris");
        student.setLastName("BORIS");
        System.out.println(student);

        System.out.println(writer.writeValueAsString(student));

        // deserialization JSON -> class instance
        Student readStudent = objectMapper.readValue("""
                {
                "first_name": "1"
                }
                """, Student.class);
        System.out.println(readStudent); // -> lastName=null

    }

    static class Student {
        @JsonProperty(value = "first_name", required = true)
        private String firstName;
        @JsonProperty("surname") // if 'value' field only - can skip its name
        private String lastName;

        public Student() {
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return "Student {" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }
}
