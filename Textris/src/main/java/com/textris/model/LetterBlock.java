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
package com.textris.model;

import storage.LetterWeights;
import java.util.Random;

public class LetterBlock {

    private final char letter;

    public LetterBlock() {
        // TODO generate letter on creation
    }

    /**
     * Generates a letter based on the weights of letters. Only called on construction.
     */
    private void GenerateLetter() {
        Random generator = new Random();

        int randomInt = generator.nextInt(LetterWeights.upperBound);
    }

    /**
     * Allows classes to access a copy of the held letter but not change it.
     *
     * @return char copy of letter
     */
    public char GetLetter() {
        // TODO return a copy of letter
    }
}
