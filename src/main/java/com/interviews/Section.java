package com.interviews;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * Represents a section of question content such as text, code, or image data.
 */
public class Section {
    private String sectionTitle;
    private ArrayList<String> sectionContent;
    private String sectionText;


    private String fileName;

    private ArrayList<String> fileLines; // Used for reading in the txt
    private BufferedImage image; // Used for reading in an image

    private contentType type; // Using Enumeration for organizing within the section
    /**
     * Creates a text-based section without an attached file or explicit content type.
     *
     * @param sectionTitle the section heading
     * @param sectionConent the structured lines stored in the section
     * @param sectionText the summary or paragraph text for the section
     */
    public Section(String sectionTitle, ArrayList<String> sectionConent, String sectionText){
        this.sectionContent = sectionConent;
        this.sectionTitle = sectionTitle;
        this.sectionText = sectionText;
    }

    /**
     * Creates a section with optional file metadata and content type.
     *
     * @param sectionTitle the section heading
     * @param sectionConent the structured lines stored in the section
     * @param sectionText the summary or paragraph text for the section
     * @param fileName the backing file name, if any
     * @param type the type of content stored in the section
     */
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

    /**
     * Returns the section title.
     *
     * @return the section title
     */
    public String getSectionTitle(){
        return sectionTitle;
    }

    /**
     * Returns the structured section content lines.
     *
     * @return the section content lines
     */
    public ArrayList<String> getSectionContent(){
        return sectionContent;
    }

    /**
     * Returns the main section text.
     *
     * @return the section text
     */
    public String getSectionText(){
        return sectionText;
    }

    /**
     * Returns the file name associated with this section.
     *
     * @return the file name, or {@code null} if none is set
     */
    public String getFileName(){
        return fileName;
    }

    /**
     * Returns the cached lines read from a file.
     *
     * @return the cached file lines
     */
    public ArrayList<String> getFileLines(){
        return fileLines;
    }

    /**
     * Returns the image stored for this section.
     *
     * @return the section image, or {@code null} if none is loaded
     */
    public BufferedImage getImage(){
        return image;
    }

    /**
     * Returns the content type used by this section.
     *
     * @return the content type
     */
    public contentType getContentType(){
        return type;
    }

    /**
     * Builds a new section from manually entered content.
     *
     * @param sectionTitle the section heading
     * @param sectionConent the section content lines
     * @param sectionText the summary or paragraph text
     * @param type the section content type
     * @return a newly created section
     */
    public Section loadContent(String sectionTitle, ArrayList<String> sectionConent, 
                                    String sectionText, contentType type){

        return new Section(sectionTitle, sectionConent, sectionText, null, type);
    }

    /**
     * Placeholder for loading section content from an external file.
     */
    public void loadContentFromFile(){

    }

}
