package com.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import com.interviews.Section;
import com.interviews.contentType;

/*
+-----------------------------------------------+-------------------------------------------------------------------+
| Test                                          | Reasoning                                                         |
+-----------------------------------------------+-------------------------------------------------------------------+
| constructor1 stores values                    | verifies title, content, and text are stored correctly            |
| constructor1 defaults                         | verifies fileName, fileLines, image, and type are null            |
| constructor2 stores values                    | verifies title, content, text, fileName, and type are stored      |
| constructor2 initializes fileLines            | verifies fileLines is initialized as an empty list                |
| constructor2 image default                    | verifies image is null                                            |
| loadContent returns new Section               | verifies a new Section object is created                          |
| loadContent sets values                       | verifies title, content, text, and type are set correctly         |
| loadContent fileName null                     | verifies returned Section has null fileName                       |
| loadContentFromFile no change                 | verifies method does not modify object (empty)                     |
+-----------------------------------------------+-------------------------------------------------------------------+
 */

public class SectionTest {

    @Test
    public void testConstructor1StoresValues() {
        ArrayList<String> content = new ArrayList<>();
        content.add("line 1");
        content.add("line 2");

        Section section = new Section("Title", content, "Text");

        assertEquals("Title", section.getSectionTitle());
        assertEquals(content, section.getSectionContent());
        assertEquals("Text", section.getSectionText());
    }

    @Test
    public void testConstructor1Defaults() {
        ArrayList<String> content = new ArrayList<>();
        Section section = new Section("Title", content, "Text");

        assertNull(section.getFileName());
        assertNull(section.getFileLines());
        assertNull(section.getImage());
        assertNull(section.getContentType());
    }

    @Test
    public void testConstructor2StoresValues() {
        ArrayList<String> content = new ArrayList<>();
        content.add("line 1");

        Section section = new Section("Title", content, "Text", "file.txt", contentType.TEXT); 

        assertEquals("Title", section.getSectionTitle());
        assertEquals(content, section.getSectionContent());
        assertEquals("Text", section.getSectionText());
        assertEquals("file.txt", section.getFileName());
        assertEquals(contentType.TEXT, section.getContentType());
    }

    @Test
    public void testConstructor2InitializesFileLines() {
        ArrayList<String> content = new ArrayList<>();
        Section section = new Section("Title", content, "Text", "file.txt", contentType.IMAGE);

        assertNotNull(section.getFileLines());
        assertEquals(0, section.getFileLines().size());
        assertNull(section.getImage());
    }

    @Test
public void testConstructor2ImageDefault() {
    ArrayList<String> content = new ArrayList<>();
    Section section = new Section("Title", content, "Text", "file.txt", contentType.IMAGE);

    assertNull(section.getImage());
}

@Test
public void testLoadContentReturnsNewSection() {
    ArrayList<String> content = new ArrayList<>();
    Section original = new Section("Old", content, "Old Text");

    Section newSection = original.loadContent("New", content, "New Text", contentType.TEXT);

    assertNotSame(original, newSection);
}

@Test
public void testLoadContentSetsValues() {
    ArrayList<String> content = new ArrayList<>();
    content.add("line");

    Section original = new Section("Old", content, "Old Text");

    Section newSection = original.loadContent("New Title", content, "New Text", contentType.TEXT);

    assertEquals("New Title", newSection.getSectionTitle());
    assertEquals(content, newSection.getSectionContent());
    assertEquals("New Text", newSection.getSectionText());
    assertEquals(contentType.TEXT, newSection.getContentType());
    assertNull(newSection.getFileName());
}
}

