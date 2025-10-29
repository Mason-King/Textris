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
    private Dictionary dictionary;
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
    public GameLoop(InputHandler inputHandler, GameBoard board, Dictionary dictionary) {
        //Init state variables
        this.board = board;
        this.dictionary = dictionary;
        this.previous = null;
        this.score = 0;
        this.gameOver = false;
      
        this.inputHandler = inputHandler;
        this.scorer = new ScoreHandler();

        this.board.setInputHandler(inputHandler);
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
            dropBlock();
        }

    }

    /**
     * Generates the current block to be dropped
     */
    public void dropBlock() {
        current = new LetterBlock();

        //Always spawn top center
        int spawnRow = 0,  spawnCol = board.getColCount() / 2;

        current.setRow(spawnRow);
        current.setCol(spawnCol);

        GameWindow.addBlock(current);

        inputHandler.setActiveBlock(current, current.getBlock());
        inputHandler.setActiveCell(board.getCell(spawnCol, spawnRow));

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
        findWords();
        previous = current;
        current = null;
        applyGravity();

        if (!inputHandler.getActiveCell().canFall())
        {
            scorer.addScore(1);
            findWords();
        }
    }

    /**
     * Sets a block in place once it reaches the bottom 
     * of the grid or another block
     */
    public void applyGravity() {
        // TODO: allow all blocks already on board to fall down
        // so that they rest at the bottom (or on top of another block)
        board.applyGravity();
    }

    /**
     * Collects strings containing the recently added block
     * and searches for words in them
     */
    public void findWords() {
        if(previous == null) return;

        GameCell cell = board.getCell(previous.getCol(), previous.getRow());
        var strings = board.detectWords(cell);

        for (String word : strings) {
            if(dictionary.isValid(word)) {
                System.out.println("Found word: " + word);
                //Remove word!
                removeWord(word, cell);
                addToScore(word.length());
            }
        }
    }

    private void removeWord(String word, GameCell startCell) {
        // Horizontal
        GameCell left = startCell;
        while (left.getLeft() != null && !left.getLeft().isEmpty()) {
            left = left.getLeft();
        }

        GameCell cur = left;
        for (int i = 0; i < word.length(); i++) {
            if (cur == null) break;
            cur.clear();
            cur = cur.getRight();
        }

        // Vertical
        GameCell top = startCell;
        while (top.getUp() != null && !top.getUp().isEmpty()) {
            top = top.getUp();
        }

        cur = top;
        for (int i = 0; i < word.length(); i++) {
            if (cur == null) break;
            cur.clear();
            cur = cur.getDown();
        }

        // Let blocks above fall down after clearing
        board.applyGravity();
    }

    private Direction opposite(Direction dir) {
        return switch (dir) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
        };
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

    public void start() {
        new Thread(() -> {
            while (!this.isGameOver()) {
                this.tick();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
//                board.printBoard();
            }
        }).start();
    }
}
