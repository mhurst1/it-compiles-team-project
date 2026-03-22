package com.interviews;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Utility class used to read external files for the application.
 * 
 * This class supports:
 * Reading text files line-by-line
 * Reading image files into BufferedImage objects
 * 
 * It is commonly used for loading content such as section text or images
 * associated with questions and solutions.
 */
public class FileReader {
    
    /**
     * Reads a text file and returns all lines as an ArrayList of Strings.
     * 
     * Each line in the file is read sequentially and stored in the list.
     * If the file cannot be found, an error message is printed.
     * 
     * @param fileName the path to the text file
     * @return an ArrayList containing each line of the file
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
     * Reads an image file and returns it as a BufferedImage object.
     * 
     * This method uses ImageIO to load the image from disk.
     * If the image cannot be read, an error message is printed
     * and null is returned.
     * 
     * Note: Requires the java.desktop module to be included.
     * 
     * @param fileName the path to the image file
     * @return a BufferedImage representing the image, or null if loading fails
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

