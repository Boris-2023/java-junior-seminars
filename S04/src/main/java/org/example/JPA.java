package org.example;

import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.List;

public class JPA {
    public static void main(String[] args) throws SQLException {
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {

            Department dept;
            try(Session session = sessionFactory.openSession()) {
                Transaction trx = session.beginTransaction();
                dept = new Department(555, "NEW DEPARTMENT");
                session.persist(dept);
                trx.commit();
            }

            // insert object into DB
            insertNewPersonIntoDB(sessionFactory, 1, "Fedya", dept);
            insertNewPersonIntoDB(sessionFactory, 2, "Vasya", dept);
            insertNewPersonIntoDB(sessionFactory, 3, "Masha", dept);
            for (long i = 4; i <= 10; i++) {
                insertNewPersonIntoDB(sessionFactory, i, "person #" + i, dept);
            }

            // read from DB
            readFromDatabaseById(sessionFactory, 1);

            // update record in DB
            readFromDatabaseById(sessionFactory, 2);
            updateRecordById(sessionFactory, 2, "Chapaev");
            readFromDatabaseById(sessionFactory, 2);

            // delete record
            System.out.println();

            deleteRecordById(sessionFactory, 3);

            checkHibernateJDBC("jdbc:h2:mem:test", "sa", "", "persons");

            // change records by filter on Id
            changeNameById(sessionFactory, ">", 5);

            printAllTable(sessionFactory);

            // persons of Department
            System.out.println("Persons by Dept: ");
            try (Session session = sessionFactory.openSession()) {
                Department department = session.get(Department.class, 555);
                department.getPersons().forEach(System.out::println);
            }
        }
    }

    private static void changeNameById(SessionFactory sessionFactory, String operator, long id) {
        try (Session session = sessionFactory.openSession()) {
            String jql = "from Person p where id " + operator + " :id";
            Query query = session.createQuery(jql, Person.class);
            query.setParameter("id", id);

            List<Person> resList = query.getResultList();

            Transaction trx = session.beginTransaction();

            resList.forEach(x -> x.setName("NewName"));

            trx.commit();
        }
    }

    private static void printAllTable(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            // JQL - Java Query Language - operates with entities and fields
            //String jql = "select p from Person p where id >= 1";
            String jql = "select p from Person p where name like '%'";
            Query query = session.createQuery(jql, Person.class);
            List resultList = query.getResultList();
            resultList.forEach(System.out::println);
        }
    }

    private static void deleteRecordById(SessionFactory sessionFactory, int id) {
        try (Session session = sessionFactory.openSession()) {

            Transaction trx = session.beginTransaction();
            Person person = session.find(Person.class, id);
            session.remove(person);
            trx.commit();

        }
    }

    private static void updateRecordById(SessionFactory sessionFactory, int id, String newName) {
        try (Session session = sessionFactory.openSession()) {
            Transaction trx = session.beginTransaction();

            Person person = session.find(Person.class, id);
            person.setName(newName);
//            session.merge(person); // .find() has already put the entity into Hiber context - no need for .merge()
//            person.setName(newName.toUpperCase());

            trx.commit();
        }
    }

    private static void readFromDatabaseById(SessionFactory sessionFactory, int id) {
        try (Session session = sessionFactory.openSession()) {
            Person person = session.get(Person.class, id);
            System.out.println(person);
        }
    }

    private static void insertNewPersonIntoDB(SessionFactory sessionFactory, long id, String name, Department dept) {
        try (Session session = sessionFactory.openSession()) {

            Transaction trx = session.beginTransaction();

            Person p = new Person();

            p.setId(id);
            p.setName(name);
            p.setDepartment(dept);

            session.persist(p);

            trx.commit();
        }
    }

    private static void checkHibernateJDBC(String url, String login, String pass, String checkTableName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, login, pass);
             Statement st = connection.createStatement()) {
            ResultSet res = st.executeQuery("select id, name from " + checkTableName);
            if (res.next()) {
                do {
                    System.out.println("Person #" +
                            res.getLong("id") + ": " +
                            res.getString("name"));
                } while (res.next());
            } else {
                System.out.println("Hibernate has started! checkHibernateStartJDBC().");
            }

        } catch (Exception e) {
            System.out.println("Hibernate failed to start! checkHibernateStartJDBC().");
        }
    }

}