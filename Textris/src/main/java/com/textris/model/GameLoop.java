/**
 * Handles the state of the game
 *
 * Responsibilities:
 * - Controls main game loop
 * - Manages game state
 *
 * Collaborators:
 * - Game Board
 * - Dictionary
 * 
 * @author Cruz Shafer
 */

package com.textris.model;

import com.textris.ui.InputHandler;

import java.util.ArrayList;
import java.util.List;

public class GameLoop 
{
    private GameBoard board;
    private Dictionary dict;
    private LetterBlock current;
    private InputHandler inputHandler;

    /**
     * Creates and manages the different aspects of the game in relation to one another
     *
     * @param board the actual grid of cells
     * @param dictionary the instance of the dictionary
     */
    public GameLoop() {
        // TODO: initialize fields
        
        // !! TESTING !!
        dict = new Dictionary();
        board = new GameBoard();
        
        //this.findWords();
    }

    /**
     * Generates the current block to be dropped
     */
    public void generateBlock() 
    {
        current = new LetterBlock();
    }

    
    /**
     * Starts dropping the current block from the top left
     */
    public void dropBlock() 
    {
        board.getStartingCell().setBlock(current);
        inputHandler.setActiveCell(board.getStartingCell());
        inputHandler.setActiveBlock(current);


    }

    /**
     * Sets a block in place once it reaches the bottom 
     * of the grid or another block
     */
    public void setBlock() 
    {
        // TODO: leave block in place, check grid for words
    }

    /**
     * Collects strings containing the recently added block
     * and searches for words in them
     */
    public void findWords() {
        // TODO: use GameBoard.detectWords() and Dictionary.isValid() to find words
        // in the grid; use GameCell.clear() to delete, drop remaining blocks
        // and call addToScore(word.length) if found
        
        //GameCell dummyCell = board.testWordSearch(); // !! NOT IDEAL, FOR TESTING ONLY !!
        GameCell dummyCell = null;
        
        List<String> newWords = new ArrayList<>();
        newWords = board.detectWords(dummyCell);
        
        for(String str : newWords){
            System.out.println("\nSearching \"" + str + "\"...\n");
            if(dict.isValid(str)){
                System.out.println("\n" + str + " is valid!");
            }
        }
        
    }

    /**
     * accessing for score field
     *
     * @return copy of score variable
     */
    public int getScore() {
        // TODO: return copy of score
        return 0;
    }

    /**
     * Adds a value to the current score
     *
     * @param bonus as the addition to the score
     */
    public void addToScore(int bonus) {
        // TODO: add bonus to score
    }
}
