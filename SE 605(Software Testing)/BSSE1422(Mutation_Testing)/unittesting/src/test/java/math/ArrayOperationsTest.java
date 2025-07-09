package math;

import io.FileIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ArrayOperationsTest {

    private ArrayOperations arrayOperations;
    private FileIO fileIO;
    private MyMath myMath;

    @BeforeEach
    void setup() {
        arrayOperations = new ArrayOperations();
        fileIO = Mockito.mock(FileIO.class);
        myMath = Mockito.mock(MyMath.class);
    }

    @Test
    void testEmptyInput() {
        when(fileIO.readFile(anyString())).thenReturn(new int[]{});

        int[] result = arrayOperations.findPrimesInFile(fileIO, "dummy", myMath);

        assertNotNull(result); // for null-return mutant
        assertEquals(0, result.length);
    }

    @Test
    void testSinglePrimeNumber() {
        when(fileIO.readFile(anyString())).thenReturn(new int[]{7});
        when(myMath.isPrime(7)).thenReturn(true);

        int[] result = arrayOperations.findPrimesInFile(fileIO, "dummy", myMath);

        assertNotNull(result);
        assertArrayEquals(new int[]{7}, result);
    }

    @Test
    void testSingleNonPrimeNumber() {
        when(fileIO.readFile(anyString())).thenReturn(new int[]{4});
        when(myMath.isPrime(4)).thenReturn(false);

        int[] result = arrayOperations.findPrimesInFile(fileIO, "dummy", myMath);

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testMixedPrimes() {
        int[] input = {2, 3, 4, 5, 8, 11};
        when(fileIO.readFile(anyString())).thenReturn(input);
        when(myMath.isPrime(2)).thenReturn(true);
        when(myMath.isPrime(3)).thenReturn(true);
        when(myMath.isPrime(4)).thenReturn(false);
        when(myMath.isPrime(5)).thenReturn(true);
        when(myMath.isPrime(8)).thenReturn(false);
        when(myMath.isPrime(11)).thenReturn(true);

        int[] result = arrayOperations.findPrimesInFile(fileIO, "dummy", myMath);

        assertArrayEquals(new int[]{2, 3, 5, 11}, result);
    }

//    @Test
//    void testAllNonPrimes() {
//        int[] input = {0, 1, 4, 6, 9, 10};
//        when(fileIO.readFile(anyString())).thenReturn(input);
//        for (int num : input) {
//            when(myMath.isPrime(num)).thenReturn(false);
//        }
//
//        int[] result = arrayOperations.findPrimesInFile(fileIO, "dummy", myMath);
//
//        assertNotNull(result);
//        assertEquals(0, result.length);
//    }

    @Test
    void testReturnArrayLengthMatchesPrimeCount() {
        when(fileIO.readFile(anyString())).thenReturn(new int[]{2, 4, 5});
        when(myMath.isPrime(2)).thenReturn(true);
        when(myMath.isPrime(4)).thenReturn(false);
        when(myMath.isPrime(5)).thenReturn(true);

        int[] result = arrayOperations.findPrimesInFile(fileIO, "dummy", myMath);

        assertEquals(2, result.length);
        assertArrayEquals(new int[]{2, 5}, result);
    }

    @Test
    void testNullSafety() {
        // simulate fileIO returning null
        when(fileIO.readFile(anyString())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            arrayOperations.findPrimesInFile(fileIO, "dummy", myMath);
        });
    }
}
