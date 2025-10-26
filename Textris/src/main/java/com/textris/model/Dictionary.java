package com.textris.model;

import storage.LoadDictionary;

/**
 * This class uses a txt file of words to check if a word has
 * formed on the grid.
 *
 * Responsibilities:
 * - References Word dictionary
 * - On block dropped, checks grid for new words
 * - Determines the point value for wordsd
 *
 * Collaborators:
 * - LoadDictionary
 *
 */
public class Dictionary {

    /**
     * Instantiates the dictionary reader (using LoadDictionary)
     */
    public Dictionary() {
        // TODO: stuff
    }

    /**
     * Checks if a string is a valid word
     *
     * @param str string to check
     */
    public boolean isValid(String str) {
        // TODO: use reference to LoadDictionary to check if a string is a word
    }
}