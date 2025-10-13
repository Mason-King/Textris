package storage;

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
public class LetterWeights {
    
    // Contains methods to allow other classes to retrieve the weight of a specific letter

    // Name of file
    private static final String fileName;

    // FIND A WAY TO STORE LETTER/WEIGHT PAIRS (map?)


    // Private construction to prevent any instances
    private LetterWeights() {
        // TODO change this? might not be best way to prevent intstantiation
    }

    /**
     * Opens file and stores the letter/weight pairs for easy access.
     *
     * @param fileName file that contains letter/weight pairs 
     */
    public static void readFile(String fileName) {
        // TODO find method for storing letter/weight pairs
    }

    /**
     * Allows classes to access a copy of the held letter but not change it.
     *
     * @param letter letter that weight is needed for 
     * @return int weight of letter
     */
    public static int GetWeight(char letter) {
        // TODO return the weight of letter
    }
}
