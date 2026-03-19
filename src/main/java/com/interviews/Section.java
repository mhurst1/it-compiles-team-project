package com.interviews;
import java.util.ArrayList;

/**
 * 
 * MH
 */
public class Section {
    private String sectionTitle;
    private ArrayList<String> sectionContent;
    private String sectionText;
    private String fileName;


    public Section(String sectionTitle, ArrayList<String> sectionConent, String sectionText){
        this.sectionContent = sectionConent;
        this.sectionTitle = sectionTitle;
        this.sectionText = sectionText;
    }

    public void fileLines(ArrayList<String> FileReader){
        
    }

    public void fileImage(ArrayList<String> FileReader){
        
    }
}
