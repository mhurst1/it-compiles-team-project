package com.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/*
+--------------------------------------+----------------------------------------------------------+
| Test                                 | Reasoning                                                |
+--------------------------------------+----------------------------------------------------------+
| write lines to file                  | saved text files should preserve each line exactly       |
| write multiline text                 | block text should be split into readable file lines      |
| writer-reader round trip             | written file output should be readable by the reader     |
+--------------------------------------+----------------------------------------------------------+
*/
public class FileWriterTest {

    @Test
    public void testWriteLinesCreatesFileWithExpectedContent() throws IOException {
        Path tempDirectory = Files.createTempDirectory("file-writer-lines");
        Path outputFile = tempDirectory.resolve("nested").resolve("output.txt");
        ArrayList<String> lines = new ArrayList<>(Arrays.asList("alpha", "beta", "gamma"));

        boolean saved = com.interviews.FileWriter.writeLines(outputFile.toString(), lines);
        List<String> savedLines = Files.readAllLines(outputFile);

        assertTrue(saved);
        assertEquals(lines, savedLines);
    }

    @Test
    public void testWriteTextSplitsMultilineTextIntoFileLines() throws IOException {
        Path tempFile = Files.createTempFile("file-writer-text", ".txt");

        boolean saved = com.interviews.FileWriter.writeText(tempFile.toString(), "line one\nline two");
        List<String> savedLines = Files.readAllLines(tempFile);

        assertTrue(saved);
        assertEquals(Arrays.asList("line one", "line two"), savedLines);
    }

    @Test
    public void testWriteLinesCanBeReadBackByFileReader() throws IOException {
        Path tempFile = Files.createTempFile("file-writer-roundtrip", ".txt");
        ArrayList<String> lines = new ArrayList<>(Arrays.asList("round", "trip", "works"));

        boolean saved = com.interviews.FileWriter.writeLines(tempFile.toString(), lines);
        ArrayList<String> loadedLines = com.interviews.FileReader.getLines(tempFile.toString());

        assertTrue(saved);
        assertEquals(lines, loadedLines);
    }
}
