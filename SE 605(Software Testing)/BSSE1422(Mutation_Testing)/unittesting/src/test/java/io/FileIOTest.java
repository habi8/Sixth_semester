package io;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileIOTest {
    private FileIO fileIO;

    @Before
    public void setUp() throws Exception {
        fileIO = new FileIO();
    }

    @Test
    public void readFile() {
        String filePath = "src/test/resources/grades_valid.txt";
        int[] actual = fileIO.readFile(filePath);
        int[] expected = {3, 9, 0, 2, 10, 9, 3, 8, 0, 3};
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_invalid_file() {
        String filePath = "src/test/resources/hello.txt";  // Non-existent file
        fileIO.readFile(filePath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_empty_file() {
        String filePath = "src/test/resources/empty_file.txt";  // Empty file
        fileIO.readFile(filePath);
    }

    // This tests the case when a directory path is passed instead of a file,
    // which triggers an IOException internally, then the empty list check throws IllegalArgumentException
    @Test(expected = IllegalArgumentException.class)
    public void readFile_with_directory_path_triggers_catch() {
        String directoryPath = "src/test/resources";  // This is a directory, not a file
        fileIO.readFile(directoryPath);
    }
}
