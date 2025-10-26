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
 * 
 * @author Cruz Shafer
 */
package com.textris.model;

import com.textris.model.LetterBlock;

// NOTE: define instance of an empty cell, to differentiate from the borders?

public class GameCell 
{
    
    private LetterBlock block;
    private LetterBlock empty = new LetterBlock('\0');

    private GameCell left, right, up, down;

    /**
     * Creates the empty GameCell that is prepared to accept a LetterBlock.
     */
    public GameCell() 
    {
        this.block = this.empty;
    }
    
    // JASON WATTS - MIGHT BE UNNECCESSARY, BUT MADE JUST IN CASE
    public GameCell(GameCell copyCell){
        this.block = new LetterBlock(copyCell.getBlock().getLetter());
        this.left = copyCell.getLeft();
        this.right = copyCell.getRight();
        this.up = copyCell.getUp();
        this.down = copyCell.getDown();
    }

    /**
     * Allows changing of held LetterBlock
     *
     * @param newBlock new block to put in
     */
    public void setBlock(LetterBlock newBlock) 
    {
        if (newBlock != null)
        {
            this.block = newBlock;
        } 
    }

    /**
     * Allows access to a copy of the LetterBlock for reading
     *
     * @return copy of letterblock
     */
    public LetterBlock getBlock() 
    {
        return this.block;
    }

    /**
     * Clears the letterblock from the gamecell, leaving it empty
     */
    public void clear() 
    {
        this.block = this.empty;
    }

    /**
     * Allows check if the GameCell is empty
     *
     * @return if empty
     */
    public boolean isEmpty() 
    {
        boolean isEmpty = false;
        
        if (this.block == this.empty)
        {
            isEmpty = true;
        }

        return isEmpty;
    }

    /**
     * Allows check if the GameCell has an occupied cell below it
     *
     * @return if the GameCell.down is occupied
     */
    public boolean canFall() 
    {
        boolean canFall = false;
        
        if (this.down.getBlock() == this.empty)
        {
            canFall = true;
        }

        return canFall;
    }


    /**
     * Allows access to left GameCell
     *
     * @return left GameCell
     */
    public GameCell getLeft()
    {
        return this.left;
    }

    /**
     * Allows access to right GameCell
     *
     * @return right GameCell
     */
    public GameCell getRight() 
    {
        return this.right;
    }

     /**
     * Allows access to upper GameCell
     *
     * @return upper GameCell
     */
    public GameCell getUp() 
    {
        return this.up; 
    }

     /**
     * Allows access to lower GameCell
     *
     * @return lower GameCell
     */
    public GameCell getDown() 
    {
        return this.down; 
    }
    


    /**
     * Allows change to left GameCell
     *
     * @param newLeft new GameCell for left
     */
    public void setLeft(GameCell newLeft) 
    {
        if (newLeft != null)
        {
            this.left = newLeft; 
        }
    }

    /**
     * Allows change to right GameCell
     *
     * @param newRight new GameCell for right
     */
    public void setRight(GameCell newRight) 
    {
        if (newRight != null)
        {
            this.right = newRight; 
        }
    }

    /**
     * Allows change to upper GameCell
     *
     * @param newUp new GameCell for up
     */
    public void setUp(GameCell newUp) 
    {
        if (newUp != null)
        {
            this.up = newUp;
        } 
    }

    /**
     * Allows change to lower GameCell
     *
     * @param newDown new GameCell for down
     */
    public void setDown(GameCell newDown) 
    {
        if (newDown != null)
        {
            this.down = newDown;
        } 
    }


    /**
     * Moves the current LetterBlock left
     */
    public void moveLeft() 
    {
        if (left.getBlock() == this.empty)
        {
            left.setBlock(this.block);
            this.block = this.empty;
        }
    }

    /**
     * Moves the current LetterBlock right
     */
    public void moveRight() 
    {
        if (right.getBlock() == this.empty)
        {
            right.setBlock(this.block);
            this.block = this.empty;
        }
    }

    /**
     * Moves the current LetterBlock down
     */
    public void moveDown() 
    {
        if (down.getBlock() == this.empty)
        {
            down.setBlock(this.block);
            this.block = this.empty;
        }
    }

}