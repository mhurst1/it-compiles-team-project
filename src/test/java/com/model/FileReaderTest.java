package com.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.junit.Test;

/*
+--------------------------------------+----------------------------------------------------------+
| Test                                 | Reasoning                                                |
+--------------------------------------+----------------------------------------------------------+
| read all lines from text file        | the reader should return every line in the same order    |
| load image file                      | image-based sections need to load file data successfully |
| missing file returns empty list      | missing text files should fail safely for callers        |
+--------------------------------------+----------------------------------------------------------+
*/
public class FileReaderTest {

    @Test
    public void testGetLinesReadsAllLinesFromTextFile() throws IOException {
        Path tempFile = Files.createTempFile("file-reader-lines", ".txt");
        Files.write(tempFile, Arrays.asList("first line", "second line", "third line"));

        ArrayList<String> lines = com.interviews.FileReader.getLines(tempFile.toString());

        assertEquals(3, lines.size());
        assertEquals("first line", lines.get(0));
        assertEquals("second line", lines.get(1));
        assertEquals("third line", lines.get(2));
    }

    @Test
    public void testGetImageLoadsImageFile() throws IOException {
        Path tempImage = Files.createTempFile("file-reader-image", ".png");
        BufferedImage expected = new BufferedImage(2, 3, BufferedImage.TYPE_INT_RGB);
        ImageIO.write(expected, "png", tempImage.toFile());

        BufferedImage actual = com.interviews.FileReader.getImage(tempImage.toString());

        assertNotNull(actual);
        assertEquals(2, actual.getWidth());
        assertEquals(3, actual.getHeight());
    }

    @Test
    public void testGetLinesReturnsEmptyListForMissingFile() {
        ArrayList<String> lines = com.interviews.FileReader.getLines("missing-file-for-reader-test.txt");

        assertNotNull(lines);
        assertTrue(lines.isEmpty());
    }
}
