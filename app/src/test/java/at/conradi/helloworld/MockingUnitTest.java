package at.conradi.helloworld;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import at.conradi.helloworld.samples.DummyClass;

import static org.junit.Assert.assertEquals;


public class MockingUnitTest {
    private DummyClass classUnderTest = null;

    /**
     * Set-up method, determined by the "before" annotation, thus used before EACH TEST method execution
     */
    @Before
    public void setUp(){
        classUnderTest = Mockito.mock(DummyClass.class);
    }

    /**
     * Test utilizing mockito, showing how to omit nested method calls. So you can isolate the unit
     * under test and eliminate influences caused by other methods
     *
     * This comes in handy e.g. to achieve isolation or if you need to test functionality,
     * relying on methods which aren't implemented yet
     *
     * The test is split into 3 parts: setup, execute, check
     */
    @Test
    public void testMethodWithNestedCall() {
        // setup
        String returnValue;
        String expectedValue = "3"; // Although real method would normally return "2"
        String errorMessage = "Values should have been the same, but %s returned and this was expected: %s";

        // execute
        Mockito.when(classUnderTest.getAsInt()).thenReturn(3);
        Mockito.when(classUnderTest.getAsString()).thenCallRealMethod();

        returnValue = classUnderTest.getAsString();

        // check
        assertEquals(String.format(errorMessage, returnValue, expectedValue), returnValue, expectedValue);
    }

    /**
     * Test utilizing mockito, showing how to mock different return values.
     *
     * This comes in handy e.g. if you need to test a method which is influenced via an additional
     * (nested) method call - this hasn't been realised via parameter handing over because of, e.g.
     * certain design reasons, API compatibility, etc. - and you need to check equivalence classes
     * for those method calls
     *
     * The test is split into 3 parts: setup, execute, check
     */
    @Test
    public void testSucceedsDueHiddenWrongValue() {
        // setup
        int returnValue;
        int expectedValue = 3;
        String errorMessage = "Values should have been the same, but %d returned and this was expected: %d";

        // execute
        Mockito.when(classUnderTest.getAsInt()).thenReturn(3);
        returnValue = classUnderTest.getAsInt();

        // check
        assertEquals(String.format(errorMessage, returnValue, expectedValue), returnValue, expectedValue);
    }


    /**
     * Test utilizing mockito, showing how to call the real method of a mocked class.
     *
     * The test is split into 3 parts: setup, execute, check
     */
    @Test
    public void testCorrectReturnValue() {
        // setup
        int returnValue;
        int expectedValue = 2;
        String errorMessage = "Values should have been the same, but '%d' has been returned and this '%d' was expected!";

        // execute
        Mockito.when(classUnderTest.getAsInt()).thenCallRealMethod();
        returnValue = classUnderTest.getAsInt();

        // check
        assertEquals(String.format(errorMessage, returnValue, expectedValue), returnValue, expectedValue);
    }

    /**
     * Test utilizing mockito, showing how to expect a specific exception.
     *
     * Expecting specific exceptions is a common approach for testing APIs for several, wrong
     * input/parameter combinations or preconditions - if e.g. not all fields of a login form
     * are filled out yet and you call an authentication service with insufficient information,
     * this service should e.g. return an Unsupported-Operation-Exception to determine the specific
     * reason for the exception.
     * This way you can check multiple exception types by generating the specific preconditions and
     * specifying the expected exception as a parameter of the annotation - another exception could
     * be some sort of ServiceNotAvailable/XYZ-Exception which could be implemented and used in case
     * of a database fault.
     *
     * The test is split into 3 parts: setup, execute, check
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testExceptionTypeUsingRealMethod() {
        // setup
        // create necessary preconditions which SHOULD cause that an exception will be thrown

        // execute
        Mockito.when(classUnderTest.someMethod()).thenCallRealMethod();

        // check
        classUnderTest.someMethod();
    }
}
