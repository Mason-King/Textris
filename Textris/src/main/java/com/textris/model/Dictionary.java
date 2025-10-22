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
 * @author Jason Watts
 */
public class Dictionary {

    /**
     * Instantiates the dictionary reader (using LoadDictionary).
     * Loads array of 3 to 5  letter words in alphabetical order into memory
     * that persists throughout the game.
     */
    public Dictionary() {

        try{
            LoadDictionary.load();
        }
        catch(java.io.IOException e){
            System.out.println("Dictionary failed to import.");
        }
    }


     /**
     * Checks if a string is a valid word
     *
     * @param str string to check
     * @return true or false whether LoadDictionary.findWord(str) found the word.
     */
    public boolean isValid(String str) {
        return LoadDictionary.findWord(str);
    }
}