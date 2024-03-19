package org.example.tests;

public class TestRunnerDemo {

    public static void main(String[] args) {
        TestRunner.run(TestRunnerDemo.class);

    }


    @Test
    void test1(){
        System.out.println("test1 running!");
    }

    @Test
    void test2(){
        System.out.println("test2 running!");
    }

    @Test
    void test3(){
        System.out.println("test3 running!");
    }
}
