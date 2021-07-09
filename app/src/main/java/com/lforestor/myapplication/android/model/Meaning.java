package com.lforestor.myapplication.android.model;

//

import java.util.ArrayList;

public class Meaning {
    private String partOfSpeech;
    private String defintion;
    private String example;
    private ArrayList<String> synonyms;

    public Meaning(String partOfSpeech, String defintion, String example, ArrayList<String> synonyms){
        this.partOfSpeech = partOfSpeech;
        this.defintion = defintion;
        this.example = example;
        this.synonyms = synonyms;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getDefintion() {
        return defintion;
    }

    public void setDefintion(String defintion) {
        this.defintion = defintion;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<String> synonyms) {
        this.synonyms = synonyms;
    }
}
