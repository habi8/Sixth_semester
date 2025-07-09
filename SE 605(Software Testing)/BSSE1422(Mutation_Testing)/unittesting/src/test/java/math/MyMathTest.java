package math;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyMathTest {

    private MyMath myMath;

    @Before
    public void setUp() {
        myMath = new MyMath();
    }

    @After
    public void tearDown() {
        myMath = null;
    }

    // --- Tests for factorial(int) ---

    @Test
    public void factorial_regular() {
        assertEquals(6, myMath.factorial(3)); // 3! = 6
    }

    @Test
    public void factorial_zero() {
        assertEquals(1, myMath.factorial(0)); // 0! = 1
    }

    @Test(expected = IllegalArgumentException.class)
    public void factorial_negative_throws() {
        myMath.factorial(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void factorial_greater_than_12_throws() {
        myMath.factorial(15);
    }

    @Test
    public void factorial_max_valid_input() {
        // 12! = 479001600
        assertEquals(479001600, myMath.factorial(12));
    }

    // --- Tests for isPrime(int) ---

    @Test(expected = IllegalArgumentException.class)
    public void isPrime_less_than_two_throws() {
        myMath.isPrime(1);
    }

    @Test
    public void isPrime_prime_number() {
        assertTrue(myMath.isPrime(7));
    }

    @Test
    public void isPrime_non_prime_number() {
        assertFalse(myMath.isPrime(8));
    }

    @Test
    public void isPrime_two_is_prime() {
        assertTrue(myMath.isPrime(2)); // smallest prime
    }

    @Test
    public void isPrime_large_prime() {
        assertTrue(myMath.isPrime(7919)); // large prime number
    }

    @Test
    public void isPrime_large_non_prime() {
        assertFalse(myMath.isPrime(7920)); // large non-prime number
    }

    // --- Boundary tests targeting loop condition at i <= n/2 ---

    @Test
    public void isPrime_number_with_factor_at_half() {
        // 10 is not prime, divisor is 5 which is 10/2
        assertFalse(myMath.isPrime(10));
    }

    @Test
    public void isPrime_prime_number_just_below_half_factor() {
        // 17 is prime, loop ends at 8 (17/2)
        assertTrue(myMath.isPrime(17));
    }

    @Test
    public void isPrime_non_prime_with_factor_just_below_half() {
        // 15 is not prime, divisor 5 < 7 (15/2)
        assertFalse(myMath.isPrime(15));
    }

    @Test
    public void isPrime_small_prime_and_non_prime_around_half() {
        assertTrue(myMath.isPrime(3));  // prime, no loop iteration needed
        assertFalse(myMath.isPrime(4)); // divisor 2 == 4/2
    }
}
