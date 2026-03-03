package com.interviews;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class FileReader {
    
    /**
     * Reads In Text File
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
     * Reads In Image File (Made Changes in module-info.java: Added, java.desktop)
     * Can take out bufferimage if that change effects anything but it should be fine
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

