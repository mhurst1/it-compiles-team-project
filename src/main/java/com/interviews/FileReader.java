package com.interviews;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Utility class for reading text and image content from files.
 */
public class FileReader {
    
    /**
     * Reads all lines from a text file.
     *
     * @param fileName the path to the text file
     * @return the file contents as a list of lines
     */
    public static ArrayList<String> getLines(String fileName) {

        ArrayList<String> lines = new ArrayList<String>();

        try {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }
            reader.close();

            
        } catch (FileNotFoundException e) {
            System.err.println("Error reading from TXT file.");
            e.printStackTrace(); // Prints the error message
        }

        return lines;
    }

    /**
     * Reads an image file into a buffered image.
     *
     * @param fileName the path to the image file
     * @return the loaded image, or {@code null} if loading fails
     */
     public static BufferedImage getImage(String fileName) {

        try{
            File file = new File(fileName);
            return ImageIO.read(file);

        } catch (IOException e){
            System.err.println("Error reading from IMAGE file.");
            e.printStackTrace(); // Prints the error message
        }

        return null; // If the image fails to load
     }
     
}

