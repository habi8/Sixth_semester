import org.junit.Test;

import static org.junit.Assert.*;

public class MyClassTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }
    @org.junit.Test
    public void div(){
        float expected = 2.0F;
        float actual = (new MyClass()).div(10,5);
        assertEquals(expected, actual,0.001);
    }

    @Test(expected = ArithmeticException.class)
    public void divideByZero(){
        (new MyClass()).div(10,0);
    }
}