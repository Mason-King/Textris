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

// package com.textris.model;
package test;

import test.LetterWeights;
import java.util.Random;

public class LetterBlock 
{
    private final char letter;

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
     * Allows classes to view the letter stored.
     *
     * @return char copy of letter
     */
    public char getLetter() {
        return letter;
    }



    public static void main(String[] args) {
        LetterBlock all;
        for (int i = 0; i < 100; i++)
        {
            all = new LetterBlock();
            System.out.println(all.getLetter() + "\n"); 
        }

    }
}
