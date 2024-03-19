package org.example.random;

public class MyTestClass {
    @RandomInteger(min = 50, max = 151)
    private int value;

    public MyTestClass() {
    }

    public static void main(String[] args) {
        MyTestClass myTestClass = new MyTestClass(); // just new instance

        for (int i = 0; i < 10; i++) {
            // checks all instance's class fields for @RandomInteger and if found,
            // checks its type is 'int', if yes -> sets Random value 0:1000
            RandomIntegerInitializer.init(myTestClass);
            System.out.println(myTestClass.getValue());
        }

    }

    public int getValue() {
        return value;
    }
}
