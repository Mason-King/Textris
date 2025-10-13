package com.textris.model;

import storage.LetterWeights;

/**
 * This class represents a block containing a letter.
 *
 * Responsibilities:
 * - Generates Random weighted letter blocks from all letters of the alphabet
 *
 * Collaborators:
 * - LetterWeights
 *
 */
public class LetterBlock {
    
    // Chooses a letter based on LetterWeights
    // Holds the letter

    private final char letter;

    public LetterBlock() {
        // TODO generate letter on creation
    }

    /**
     * Generates a letter based on the weights of letters. Only called on construction.
     */
    private void GenerateLetter() {
        // TODO assign a letter based on weights in storage.LetterWeights
    }

    /**
     * Allows classes to access a copy of the held letter but not change it.
     *
     * @return string copy of letter
     */
    public String GetLetter() {
        // TODO return a copy of letter
    }
}
