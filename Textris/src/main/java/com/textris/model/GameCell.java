package com.textris.model;

import com.textris.model.LetterBlock;

/**
 * This class represents a cell in the GameBoard that holds a
 * LetterBlock.
 *
 * Responsibilities:
 * - Holds the value of a main LetterBlock
 * - Holds the values of neighboring LetterBlocks
 *
 * Collaborators:
 * - LetterBlock
 * - GameBoard
 */
public class GameCell {
    
    // The block that is hold in the cell
    private LetterBlock block;

    // The cells neighboring the main cell
    // Add diagonals if needed
    private GameCell left;
    private GameCell right;
    private GameCell up;
    private GameCell down;

    /**
     * Creates the empty GameCell that is prepared to accept a 
     * LetterBlock.
     *
     * @param main the initial LetterBlock stored (usually null?)
     */
    public GameCell(LetterBlock block) {
        // TODO: allow instantiation to null LetterBlock
    }

    /**
     * Allows changing of held LetterBlock
     *
     * @param newBlock new block to put in
     */
    public void setBlock(LetterBlock newBlock) {
        // TODO: set LetterBlock block to the newBlock
    }

    /**
     * Allows access to a copy of the LetterBlock for reading
     *
     * @return copy of letterblock
     */
    public LetterBlock getBlock() {
        // TODO: return copy of letterblock main
    }

    /**
     * Clears the letterblock from the gamecell, leaving it empty
     */
    public void clear() {
        // TODO: clear letterblock from gamecell
    }

    /**
     * Allows check if the GameCell is empty
     *
     * @return if empty
     */
    public boolean isEmpty() {
        // TODO: return true if LetterBlock main == null, false if not
    }


    /**
     * Allows access to left GameCell
     *
     * @return left GameCell
     */
    public GameCell getLeft() {
        // TODO: return GameCell left 
    }

    /**
     * Allows access to right GameCell
     *
     * @return right GameCell
     */
    public GameCell getRight() {
        // TODO: return GameCell right 
    }

     /**
     * Allows access to upper GameCell
     *
     * @return upper GameCell
     */
    public GameCell getUp() {
        // TODO: return GameCell up 
    }

     /**
     * Allows access to lower GameCell
     *
     * @return lower GameCell
     */
    public GameCell getDown() {
        // TODO: return GameCell lown 
    }
    


    /**
     * Allows change to left GameCell
     *
     * @param newLeft new GameCell for left
     */
    public void setLeft(GameCell newLeft) {
        // TODO: check input, assign to GameCell left 
    }

    /**
     * Allows change to right GameCell
     *
     * @param newRight new GameCell for right
     */
    public void setRight(GameCell newRight) {
        // TODO: check input, assign to GameCell right 
    }

    /**
     * Allows change to upper GameCell
     *
     * @param newUp new GameCell for up
     */
    public void setUp(GameCell newUp) {
        // TODO: check input, assign to GameCell up 
    }

    /**
     * Allows change to lower GameCell
     *
     * @param newDown new GameCell for down
     */
    public void setDown(GameCell newDown) {
        // TODO: check input, assign to GameCell down 
    }


    /**
     * Moves the current LetterBlock left
     */
    public void moveLeft() {
        // TODO: if there's nothing in GameCell.left,
        // move LetterBlock block there
    }

    /**
     * Moves the current LetterBlock right
     */
    public void moveRight() {
        // TODO: if there's nothing in GameCell.right,
        // move LetterBlock block there
    }

    /**
     * Moves the current LetterBlock down
     */
    public void moveDown() {
        // TODO: if there's nothing in GameCell.down,
        // move LetterBlock block there
    }

}
