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
    private LetterBlock previous;
    private int score;
    private boolean gameOver;

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
    public GameLoop(GameBoard board, Dictionary dictionary) {
        this.board = board;
        this.dictionary = dictionary;
        this.previous = null;
        this.score = 0;
      
        inputHandler = new InputHandler(GameWindow.getScene(), board);
        scorer = new ScoreHandler();
    }

    /**
     * Called each tick to update the screen/move blocks
     */
    public void tick() {
        if (gameOver) return;

        if(current == null) {
            dropBlock();
            return;
        }

        if (board.canMove(current, Direction.DOWN)) {
            board.move(current, Direction.DOWN);
        } else {
            setBlock();
        }

        board.printBoard();
    }

    /**
     * Generates the current block to be dropped
     */
    public void dropBlock() {
        // TODO: implement block-dropping logic - Need to check can drop on board
        current = new LetterBlock();

        //Always spawn top center
        int spawnRow = 0,  spawnCol = board.getColCount() / 2;

        current.setRow(spawnRow);
        current.setCol(spawnCol);

        //placeBlock returns a boolean
        if (!board.placeBlock(current)) {
            System.out.println("GAME OVER");
            current = null;
            gameOver = true;
            //TODO: implement ending logic
        }
    }

    
    /**
     * Starts dropping the current block from the top left
     */
    public void setBlock() {
        previous = current;
        current = null;
        findWords();
        fallingBlocks();
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
    public void fallingBlocks() {
        // TODO: allow all blocks already on board to fall down
        // so that they rest at the bottom (or on top of another block)
        board.applyGravity();
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
        var words = board.detectWords();

        for (String word : words) {
            if(dictionary.isValid(word)) {
                //TODO: Clear word from board
                //Add to score
                addToScore(0);
            }
        }
        fallingBlocks();
    }

    /**
     * accessing for score field
     *
     * @return copy of score variable
     */
    public int getScore() {
        return score;
    }

    /**
     * Adds a value to the current score
     *
     * @return if the game is running
     */
    public void addToScore(int bonus) {
        score += bonus;
    }

    public void moveCurrent(Direction direction) {
        if (current != null && board.canMove(current, direction)) {
            board.move(current, direction);
        }
    }

    public void dropToBottom() {
        while (current != null && board.canMove(current, Direction.DOWN)) {
            board.move(current, Direction.DOWN);
        }
        setBlock();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void reset() {
        //TODO: Implement board clearing
        this.score = 0;
        this.current = null;
        this.previous = null;
        this.gameOver = false;
    }
}
