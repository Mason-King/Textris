package com.textris.model;

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
 */

public class GameLoop {

    // Create a new gameboard of the correct size
    private GameBoard board;
    private Dictionary dictionary;
    private LetterBlock current;
    private int score;

    /**
     * Creates and manages the different aspects of the game in relation to one another
     *
     * @param board the actual grid of cells
     * @param dictionary the instance of the dictionary
     */
    public GameLoop(GameBoard board, Dictionary dictionary) {
        // TODO: initialize fields
    }
    
    /**
     * Starts dropping the current block from the top left
     */
    public void dropBlock() {
        // TODO: implement block-dropping logic
    }

    /**
     * Sets a block in place once it reaches the bottom 
     * of the grid or another block
     */
    public void setBlock() {
        // TODO: leave block in place, check grid for words
    }

    /**
     * Allows the rest of the blocks to fall to the bottom after deleting some
     */
    public void fallingBlocks() {
        // TODO: allow all blocks already on board to fall down
        // so that they rest at the bottom (or on top of another block)
    }

    /**
     * Collects strings containing the recently added block
     * and searches for words in them
     */
    public void findWords() {
        // TODO: use GameBoard.detectWords() and Dictionary.isValid() to find words
        // in the grid; use GameCell.clear() to delete, drop remaining blocks
        // and call addToScore(word.length) if found
    }

    /**
     * Accessor for score field
     *
     * @return copy of score variable
     */
    public int getScore() {
        // TODO: return copy of score
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
