package com.interviews;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * 
 * MH
 */
public class Section {
    private String sectionTitle;
    private ArrayList<String> sectionContent;
    private String sectionText;


    private String fileName;

    private ArrayList<String> fileLines; // Used for reading in the txt
    private BufferedImage image; // Used for reading in an image

    private contentType type; // Using Enumeration for organizing within the section


    // I have this as a placeholder for the DataLoader Class. We will have to update the data loader 
    public Section(String sectionTitle, ArrayList<String> sectionConent, String sectionText){
        this.sectionContent = sectionConent;
        this.sectionTitle = sectionTitle;
        this.sectionText = sectionText;
    }

    public Section(String sectionTitle, ArrayList<String> sectionConent, 
                String sectionText, String fileName, contentType type){
        this.sectionContent = sectionConent;
        this.sectionTitle = sectionTitle;
        this.sectionText = sectionText;
        this.fileName = fileName;
        this.type = type;

        // For the Files
        this.fileLines = new ArrayList<>();
        this.image = null;
    }

    // Getters that may need to be used
    public String getSectionTitle(){
        return sectionTitle;
    }
    public ArrayList<String> getSectionContent(){
        return sectionContent;
    }
    public String getSectionText(){
        return sectionText;
    }
    public String getFileName(){
        return fileName;
    }
    public ArrayList<String> getFileLines(){
        return fileLines;
    }
    public BufferedImage getImage(){
        return image;
    }
    public contentType getContentType(){
        return type;
    }

    
    /**
     * I added 2 seperate ways to create the sections. One for if we want to do it reading from a file.
     * And a seperate one for if we want to manually enter the data ourselves
     */


    // This will be if the user wants to load there data themselves by typing it in
    public Section loadContent(String sectionTitle, ArrayList<String> sectionConent, 
                                    String sectionText, contentType type){

        return new Section(sectionTitle, sectionConent, sectionText, null, type);
    }

    // This will be if the user wants to load there data from a file (Im Leaving This Blank For Now)
    public void loadContentFromFile(){

    }

}
