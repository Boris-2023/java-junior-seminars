// 1. Создать сущность Student с полями:
//1.1 id - int
//1.2 firstName - string
//1.3 secondName - string
//1.4 age - int
//2. Подключить hibernate. Реализовать простые запросы: Find(by id), Persist, Merge, Remove
//3. Попробовать написать запрос поиска всех студентов старше 20 лет (session.createQuery)

package org.example;

import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class JPA {
    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {

            // insert students into Table - .persist()
            insertRandomStudents(sessionFactory, 10);

            // update student by id - .merge()
            updateStudentById(sessionFactory, 5, "Vasya", "Vasin");

            // print student by id - .find()
            printStudentById(sessionFactory, 5);

            // remove student by id - .remove()
            removeStudentById(sessionFactory, 2);

            // print all students from the table - .createQuery()
            System.out.println("\nAll students:");
            printQueryResultJQL(sessionFactory, "age", ">", 0);

            // print students aged 20+ - .createQuery()
            System.out.println("\nStudents who are older than 20:");
            printQueryResultJQL(sessionFactory, "age", ">", 20);

            // print Vasya - .createQuery()
            System.out.println("\nPrint Vasya:");
            printQueryResultJQL(sessionFactory, "firstName", "like", "Vasya");

        }
    }

    private static <T> void printQueryResultJQL(SessionFactory sessionFactory, String field, String operator, T parameter) {
        try (Session session = sessionFactory.openSession()) {
            String jql = "from Student where " + field + " " + operator + " :parameter";
            Query query = session.createQuery(jql, Student.class);
            query.setParameter("parameter", parameter);
            List<Student> resList = query.getResultList();
            resList.forEach(System.out::println);
        }
    }

    private static void removeStudentById(SessionFactory sessionFactory, int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction trx = session.beginTransaction();
            Student student = session.find(Student.class, id);
            session.remove(student);
            trx.commit();
        }
    }

    private static void updateStudentById(SessionFactory sessionFactory, int id, String firstName, String secondName) {
        try (Session session = sessionFactory.openSession()) {
            Transaction trx = session.beginTransaction();

            Student student = session.find(Student.class, id);
            student.setFirstName(firstName);
            student.setSecondName(secondName);
            session.merge(student); // can skip as .find() already put student into the context

            trx.commit();
        }
    }

    private static void printStudentById(SessionFactory sessionFactory, int id) {
        try (Session session = sessionFactory.openSession()) {
            Student student = session.find(Student.class, id);
            System.out.println(student);
        }
    }

    private static void insertRandomStudents(SessionFactory sessionFactory, int studentsNumber) {
        try (Session session = sessionFactory.openSession()) {
            Transaction trx = session.beginTransaction();
            for (int i = 1; i <= studentsNumber; i++) {
                Student student = new Student(
                        "FirstName #" + i,
                        "LastName #" + i,
                        ThreadLocalRandom.current().nextInt(18, 28)
                );
                session.persist(student);
            }
            trx.commit();
        }
    }

}