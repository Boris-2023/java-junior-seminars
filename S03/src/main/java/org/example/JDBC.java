package org.example;

import java.sql.*;

public class JDBC {
    public static void main(String[] args) {
        // JDBC - Java DataBase Connectivity
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:h2:mem:test");

            acceptConnection(connection);

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void acceptConnection(Connection connection) throws SQLException {
        createTable(connection);
        insertRecords(connection);
        updateRecord(connection, "Ivan", "John");
        deleteRecord(connection);
        readData(connection);
    }

    private static void deleteRecord(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int res = statement.executeUpdate("delete from person where id = 2");

            System.out.println("DELETE: records affected = " + res);
        }
    }

    private static void updateRecord(Connection connection, String name, String surname) throws SQLException {
        // ----> SAFE option - no injection allowed!
        try (PreparedStatement stmt = connection.prepareStatement(
                "update person set name = $2 where surname = $1")) {
            stmt.setString(2, name);
            stmt.setString(1, surname);

            System.out.println("UPDATE: records affected = " + stmt.executeUpdate());
        }

        // ----> UNSAFE for injection: surname = "3 or 1=1" => change all names as 1=1 is true for all records!
//        try (Statement statement = connection.createStatement()) {
//            int res = statement.executeUpdate("update person set name = '" + name + "' where surname = '" + surname + "'");
//
//            System.out.println("UPDATE: affected records = " + res);
//        }
    }

    private static void readData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
                    select id, name, surname from person
                    where id < 5
                    """);

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");

                System.out.println("id = " + id + " -> name = " + name + ", surname = " + surname);
            }
        }
    }

    private static void insertRecords(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int res = statement.executeUpdate("""
                    insert into person(id, name, surname) values
                    (1, 'Boris', 'Boris'),
                    (2, 'Sergey', 'Sergey'),
                    (3, 'John', 'John'),
                    (4, 'Petr', 'Petr'),
                    (5, 'Chapaev', 'Chapaev')
                    """);

            System.out.println("INSERT: records affected = " + res);
        }

    }

    private static void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    create table Person(
                    id bigint,
                    name varchar (256),
                    surname varchar (256)
                    )
                    """);
        }
    }
}