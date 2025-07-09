package math;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArithmeticOperationsTest {

    private final ArithmeticOperations ops = new ArithmeticOperations();

    // --- Tests for divide(double, double) ---

    @Test
    public void divide_regular() {
        assertEquals(5.0, ops.divide(10, 2), 1e-6);
    }

    @Test
    public void divide_zero_numerator() {
        assertEquals(0.0, ops.divide(0, 10), 1e-6);
    }

    @Test
    public void divide_negative_numerator() {
        assertEquals(-5.0, ops.divide(-10, 2), 1e-6);
    }

    @Test
    public void divide_both_negative() {
        assertEquals(5.0, ops.divide(-10, -2), 1e-6);
    }

    @Test(expected = ArithmeticException.class)
    public void divide_by_zero_should_throw() {
        ops.divide(10, 0);
    }

    // --- Tests for multiply(int, int) ---

    @Test
    public void multiply_regular() {
        assertEquals(20, ops.multiply(10, 2));
    }

    @Test
    public void multiply_typical_values() {
        assertEquals(6, ops.multiply(2, 3));
        assertEquals(15, ops.multiply(3, 5));
        assertEquals(100, ops.multiply(10, 10));
    }

    @Test
    public void multiply_zero() {
        assertEquals(0, ops.multiply(0, 100));
        assertEquals(0, ops.multiply(100, 0));
    }

    @Test
    public void multiply_one() {
        assertEquals(10, ops.multiply(10, 1));
        assertEquals(10, ops.multiply(1, 10));
    }

    @Test
    public void multiply_edge_values_just_below_overflow() {
        int x = Integer.MAX_VALUE / 2;
        int y = 2;
        assertEquals(x * y, ops.multiply(x, y));
    }

    @Test
    public void multiply_with_max_value_and_one() {
        assertEquals(Integer.MAX_VALUE, ops.multiply(Integer.MAX_VALUE, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void multiply_with_negative_first_arg() {
        ops.multiply(-10, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void multiply_with_negative_second_arg() {
        ops.multiply(10, -2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void multiply_with_both_negative_args() {
        ops.multiply(-10, -2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void multiply_max_value_overflow() {
        ops.multiply(Integer.MAX_VALUE, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void multiply_overflow_large_numbers() {
        ops.multiply(100_000, 100_000);
    }
}
