package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Reflections {
    public static void main(String[] args) throws Exception {

        // common way to create a new instance
        User boris_common = new User("Boris", "abc");

        // creating new instance through Reflection
        Class<User> userClass = User.class;
        Constructor<User> constructor = userClass.getConstructor(String.class, String.class);
        User boris = constructor.newInstance("Boris", "123");

        System.out.println("\nTotal users number: " + User.getCounter());
        System.out.println(boris_common);
        System.out.println(boris);

        // call method getLogin()
        Method mGetLogin = userClass.getMethod("getLogin");
        String login = (String) mGetLogin.invoke(boris); // вызвать на объекте (Reflection)
        System.out.println(login
                + " == "
                + boris.getLogin()); // the same result

        // setPassword
        Method mSetPass = userClass.getMethod("setPassword", String.class);
        mSetPass.invoke(boris, "newPassword");
        System.out.println(boris); // new password applied


        // static methods (here getCounter() of class User)
        Method mGetCounter = userClass.getMethod("getCounter");
        System.out.println("Total users: " + mGetCounter.invoke(null));


        // read the fields
        Field[] fields = userClass.getDeclaredFields(); // all the fields available in the class
        Arrays.stream(fields).forEach(x -> System.out.print(x.getName() + " "));

        Field pwd = userClass.getDeclaredField("password"); // particular fields
        System.out.println("\n" + pwd.getName() + " = " + pwd.get(boris));

        // change final field !!!
        Field logIn = userClass.getDeclaredField("login");
        logIn.setAccessible(true);
        logIn.set(boris, "BORIS");

        System.out.println(boris);

        // more features
        Class<? super User> superClass = User.class.getSuperclass(); // finds a super class for User - to work on with it
        System.out.println(superClass.getSimpleName());

    }

    static class User {

        private static long counter = 0L;
        private final String login;
        private String password;
        private long uid;

        public User(String login) {
            this(login, "defaultPassword");
        }

        public User(String login, String password) {
            this.login = login;
            this.password = password;
            counter++;
            uid = counter;
        }

        public static long getCounter() {
            return counter;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "User #" + uid + ": {" +
                    "login='" + login + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    static class SuperUser extends User {
        public SuperUser(String login) {
            super(login, "");
        }

        @Override
        @MyAnnotation(myParameter = "text")
        public void setPassword(String password) {
            throw new UnsupportedOperationException();
        }


    }
}