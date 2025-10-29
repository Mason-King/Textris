/**
 * This class contains a reference to a file that holds the
 * weighted list of letters.
 *
 * Responsibilities:
 * - Allows other classes to reference letter weights
 *
 * Collaborators:
 *
 * @author Cruz Shafer
 */

package com.textris.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.TreeMap;

public class LetterWeights 
{
    // file of cumulative weighted regions from 0 to 9999
    private static final String fileName = "/weightsCumulative.txt";

    private static TreeMap<Integer, Character> weights = new TreeMap<>();

    static 
    {
        readFile(fileName);
    }


    // Private construction to prevent any instances
    private LetterWeights() 
    {
//        So what iv done:
//        - When you create a new LetterBlock() it creates the `Block` so were not doing two things there to avoid confusion, and stores it within the letterblock
    }

    /**
     * Opens file and stores the letter/weight pairs for easy access.
     *
     * @param fileName file that contains letter/weight pairs 
     */
    public static void readFile(String fileName) 
    {
        try (InputStream input = LetterWeights.class.getResourceAsStream(fileName))
        {
            try (Scanner reader = new Scanner(input))
            {
                char currentLetter = 'a';

                while (reader.hasNextLine())
                {
                    String data = reader.nextLine();
                    int weight = Integer.parseInt(data);

                    weights.put(weight, currentLetter);
                    currentLetter++;
                }
            } 
        } 
        catch (IOException exception)
        {
            System.out.println("LetterWeights file could not be found.");
            exception.printStackTrace();
        }
    }

     /**
     * Allows classes to generate a letter based off of an input number.
     *
     * @param number number that determines letter 
     * @return char letter
     */
    public static char getLetter(int number) 
    {
        return weights.ceilingEntry(number).getValue();
    }


    /**
     * Allows classes to access the upperBound of the number range that needs to be generated.
     *
     * @return int upperBound
     */
    public static int getUpperBound() 
    {
        return 10000;
    }




    public static void main(String[] args)
    {
        
    }
}