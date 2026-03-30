package com.interviews;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class used to write text data to external files for the application.
 */
public class FileWriter {

    /**
     * Writes each entry in {@code lines} to the target file as its own line.
     * Creates parent directories when needed.
     *
     * @param fileName the path to the text file
     * @param lines the lines to write
     * @return {@code true} when the file is written successfully
     */
    public static boolean writeLines(String fileName, ArrayList<String> lines) {
        Path path = Paths.get(fileName);
        List<String> safeLines = new ArrayList<>();

        if (lines != null) {
            for (String line : lines) {
                safeLines.add(line == null ? "" : line);
            }
        }

        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(path, safeLines, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to TXT file.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Writes a single block of text to the target file.
     *
     * @param fileName the path to the text file
     * @param text the text content to write
     * @return {@code true} when the file is written successfully
     */
    public static boolean writeText(String fileName, String text) {
        ArrayList<String> lines = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return writeLines(fileName, lines);
        }

        String[] splitText = text.split("\\R", -1);
        for (String line : splitText) {
            lines.add(line);
        }

        return writeLines(fileName, lines);
    }
}
