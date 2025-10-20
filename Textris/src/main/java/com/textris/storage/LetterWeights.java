/**
 * This class contains a reference to a file that holds the
 * weighted list of letters.
 *
 * Responsibilities:
 * - Allows other classes to reference letter weights
 *
 * Collaborators:
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class LetterWeights {
    
    // file of cumulative weighted regions from 0 to 9999
    private static final String fileName = "weightsCumulative.txt";

    private static Map<Character, Integer> weights = new HashMap<>();

    private static upperBound = 10000;

    // Private construction to prevent any instances
    private LetterWeights() {  }

    /**
     * Opens file and stores the letter/weight pairs for easy access.
     *
     * @param fileName file that contains letter/weight pairs 
     */
    public static void readFile(String fileName) {

        File weightsFile = new File(fileName);

        try (Scanner reader = new Scanner(weightsFile))
        {
            char currentLetter = 'a';

            while (reader.hasNextLine())
            {
                String data = reader.nextLine();
                int weight = Integer.parseInt(data);

                weights.put(currentLetter, weight);
                currentLetter++;
            }

            System.out.println("Map elements: " + weights + " .\n");
        } catch (FileNotFoundException exception)
        {
            System.out.println("LetterWeights file could not be found.");
            exception.printStackTrace();
        }
        
    }

    /**
     * Allows classes to access a copy of the held letter but not change it.
     *
     * @param letter letter that weight is needed for 
     * @return int weight of letter
     */
    public static int getWeight(char letter) {
        letter = Character.toLowerCase(letter);

        return weights.get(letter);
    }


     /**
     * Allows classes to generate a letter based off of an input number.
     *
     * @param number number that determines letter 
     * @return char letter
     */
    public static char getLetter(int number) {
        

 //       return search;
    }


    // public static void main(String[] args) {
        
    //     readFile(fileName);
    //     System.out.println("Weight for L is " + getWeight('l') + ".\n");
    //     System.out.println("Weight for D is " + getWeight('D') + ".\n");


    // }
}
