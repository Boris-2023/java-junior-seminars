
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestRunner {
    static List<Method> tests = new ArrayList<>();
    static List<Method> beforeAll = new ArrayList<>();
    static List<Method> afterAll = new ArrayList<>();
    static List<Method> beforeEach = new ArrayList<>();
    static List<Method> afterEach = new ArrayList<>();

    public static void run(Class<?> testClass) throws IllegalAccessException, InvocationTargetException {

        getMethodsFromClass(testClass);

        final Object testObject = initTestObject(testClass);

        for (Method method : beforeAll) {
            method.invoke(testObject);
        }

        for (Method method : sortTestMethodsByParameter(tests)) {
            for (Method adMethod : beforeEach) {
                adMethod.invoke(testObject);
            }
            method.invoke(testObject);
            for (Method adMethod : afterEach) {
                adMethod.invoke(testObject);
            }
        }

        for (Method method : afterAll) {
            method.invoke(testObject);
        }

    }

    private static void getMethodsFromClass(Class<?> testClass) {

        for (Method method : testClass.getDeclaredMethods()) {

            if (method.getModifiers() == Modifier.PRIVATE) {
                continue;
            }

            if (method.getAnnotation(Test.class) != null) {
                tests.add(method);
            }
            if (method.getAnnotation(BeforeAll.class) != null) {
                beforeAll.add(method);
            }
            if (method.getAnnotation(AfterAll.class) != null) {
                afterAll.add(method);
            }
            if (method.getAnnotation(BeforeEach.class) != null) {
                beforeEach.add(method);
            }
            if (method.getAnnotation(AfterEach.class) != null) {
                afterEach.add(method);
            }
        }
    }

    private static List<Method> sortTestMethodsByParameter(List<Method> tests) {
        Comparator<Method> comparator = Comparator.comparingInt(x -> x.getAnnotation(Test.class).order());
        return tests.stream().sorted(comparator).toList();
    }

    private static Object initTestObject(Class<?> testClass) {
        try {
            Constructor<?> noArgsConstructor = testClass.getConstructor();
            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("There is no by-default constructor!");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create an instance of test class!");
        }
    }


}
