import java.lang.reflect.InvocationTargetException;

//Доделать запускатель тестов:
//1. Создать аннотации BeforeEach, BeforeAll, AfterEach, AfterAll
//2. Доработать класс TestRunner так, что
//2.1 Перед всеми тестами запускаеются методы, над которыми стоит BeforeAll
//2.2 Перед каждым тестом запускаются методы, над которыми стоит BeforeEach
//2.3 Запускаются все тест-методы (это уже реализовано)
//2.4 После каждого теста запускаются методы, над которыми стоит AfterEach
//2.5 После всех тестов запускаются методы, над которыми стоит AfterAll
//Другими словами, BeforeAll -> BeforeEach -> Test1 -> AfterEach -> BeforeEach -> Test2 -> AfterEach -> AfterAll
//
//3.* Доработать аннотацию Test: добавить параметр int order,
//по котрому нужно отсортировать тест-методы (от меньшего к большему) и запустить в нужном порядке.
//Значение order по умолчанию - 0
//4.** Создать класс Asserter для проверки результатов внутри теста с методами:
//4.1 assertEquals(int expected, int actual)
//Идеи реализации: внутри Asserter'а кидать исключения, которые перехвываются в тесте.
//Из TestRunner можно возвращать какой-то объект, описывающий результат тестирования.
public class Homework {

    public static void main(String[] args) {
        try {
            TestRunner.run(Homework.class);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    @Test(order = 5)
    void test1() {
        System.out.println("test #1 is running...");
    }

    @Test(order = 2)
    void test2() {
        System.out.println("test #2 is running...");
    }

    @Test
    void test3() {
        System.out.println("test #3 is running...");
    }

    @BeforeAll
    static void beforeAll(){
        System.out.println("Starting all tests ...\n");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("All tests are finished!");
    }

    @BeforeEach
    static void beforeEach(){
        System.out.println("Starting new test: ");
    }

    @AfterEach
    static void afterEach(){
        System.out.println("Test ended!\n");
    }

}
