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

import com.textris.media.Block;
import com.textris.ui.GameWindow;
import com.textris.ui.InputHandler;

import java.util.ArrayList;
import java.util.List;

public class GameLoop 
{
    private GameBoard board;
    private Dictionary dict;
    private LetterBlock current;
    private Block fallingBlock;
    private InputHandler inputHandler;
    private ScoreHandler scorer;

    private boolean gameOn;

    /**
     * Creates and manages the different aspects of the game in relation to one another
     *
     * @param board the actual grid of cells
     * @param dictionary the instance of the dictionary
     */
    public GameLoop() 
    {
        dict = new Dictionary();
        board = new GameBoard();
        inputHandler = new InputHandler(GameWindow.getScene(), board);
        scorer = new ScoreHandler();
    }

    /**
     * Generates the current block to be dropped
     */
    public void generateBlock() 
    {
        current = new LetterBlock();
        fallingBlock = new Block(current.getLetter());
    }

    
    /**
     * Starts dropping the current block from the top left
     */
    public void dropBlock() 
    {
        board.getStartingCell().setBlock(current);
        inputHandler.setActiveCell(board.getStartingCell());
        inputHandler.setActiveBlock(current, fallingBlock);


    }

    /**
     * Sets a block in place once it reaches the bottom 
     * of the grid or another block
     */
    public void setBlock() 
    {
        if (!inputHandler.getActiveCell().canFall())
        {
            // TODO: stop inputHandler from trying to move the cell
            scorer.addScore(1);
            findWords();
            board.layThemToRest();
        }
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
     * Access for game state
     *
     * @return if the game is running
     */
    public boolean getGameOn() 
    {
        return gameOn;
    }
}
