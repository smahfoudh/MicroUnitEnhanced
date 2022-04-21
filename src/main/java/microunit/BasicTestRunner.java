package microunit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for running unit tests without support for expected exceptions.
 */
public class BasicTestRunner extends TestRunner {


    /**
     * Creates a {@code BasicTestRunner} object for executing the test methods
     * of the class specified.
     *
     * @param testClass the class whose test methods will be executed
     */
    public BasicTestRunner(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public void invokeTestMethod(Method testMethod, Object instance, TestResultAccumulator results)
            throws IllegalAccessException {
        try {
            testMethod.invoke(instance);
            results.onSuccess(testMethod);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            cause.printStackTrace(System.out);
            if (cause instanceof AssertionError) {
                results.onFailure(testMethod);
            } else {
                results.onError(testMethod);
            }
        }
    }
    private static Logger logger = LoggerFactory.getLogger(BasicTestRunner.class);

    // CHECKSTYLE:OFF
    public static void main(String[] args) throws Exception {
        logger.info("Java version is {}", System.getProperty("java.version"));
        logger.error("An exception occurred", new RuntimeException("Oops, something went wrong"));
        Class<?> testClass = Class.forName(args[0]);
        new BasicTestRunner(testClass).runTestMethods();


    }



}
