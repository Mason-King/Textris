package com.textris.model;

import com.textris.storage.LoadDictionary;

/**
 * This class uses a text file of words to check if a word has
 * formed on the grid.
 *
 * Responsibilities:
 * - References Word dictionary
 * - On block dropped, checks grid for new words
 * - Determines the point value for words
 *
 * Collaborators:
 * - LoadDictionary
 *
 */
public class Dictionary {

    private final LoadDictionary dictionary;

    public Dictionary() {
        this.dictionary = new LoadDictionary();

        try{
            dictionary.load();
        }
        catch(java.io.IOException e){
            System.out.println("Dictionary failed to import.");
        }
    }

/**
     * Instantiates the dictionary reader (using LoadDictionary)
     */
        /**
     * Checks if a string is a valid word
     *
     * @param str string to check
     * @return 
     */
    public boolean isValid(String str) {
        return dictionary.findWord(str);
    }
}