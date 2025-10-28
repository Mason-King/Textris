/**
 * This class represents a block containing a letter.
 *
 * Responsibilities:
 * - Generates Random weighted letter blocks from all letters of the alphabet
 *
 * Collaborators:
 * - LetterWeights
 *
 * @author Cruz Shafer
 */

package com.textris.model;

import com.textris.storage.LetterWeights;
import java.util.Random;

public class LetterBlock 
{
    private final char letter;
    private int row;
    private int col;

    /**
     * Generates a letter based on the weights of letters carried in LetterWeights. Only called on construction.
     */
    public LetterBlock() 
    {
        Random generator = new Random();

        int randomInt = generator.nextInt(LetterWeights.getUpperBound());

        letter = LetterWeights.getLetter(randomInt);
    }
    
    /**
     * Overloaded parameter that initiates a placeholder LetterBlock
     * 
     * @param code if 0, exists as a placeholder block. otherwise useless
     */
    public LetterBlock(char code) 
    {
        letter = code;
    }

    /**
     * Allows classes to view the letter stored.
     *
     * @return char copy of letter
     */
    public char getLetter() {
        return letter;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}