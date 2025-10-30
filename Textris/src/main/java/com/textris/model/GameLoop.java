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

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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
    private boolean boardBusy = false;

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

        // 🛑 Skip tick if board is busy clearing words or applying gravity
        if (boardBusy) return;

        if (current == null) {
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
        if (boardBusy) return; // 🛑 skip if board is busy

        current = new LetterBlock();

        int spawnRow = 0, spawnCol = board.getColCount() / 2;

        current.setRow(spawnRow);
        current.setCol(spawnCol);

        GameWindow.addBlock(current);

        inputHandler.setActiveBlock(current, current.getBlock());
        inputHandler.setActiveCell(board.getCell(spawnCol, spawnRow));

        if (!board.placeBlock(current)) {
            System.out.println("GAME OVER");
            current = null;
            gameOver = true;
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
        if (previous == null) return;

        GameCell cell = board.getCell(previous.getCol(), previous.getRow());

    List<GameBoard.WordMatch> matches = board.detectWords(cell);

        for (GameBoard.WordMatch match : matches) {
            System.out.println("Found word: " + match.word + " dir=" + match.dir);
            // Clear via the removeWord that accepts a startCell + direction
            removeWord(match.word, match.startCell, match.dir);
            addToScore(match.word.length());
        }
    }

    /**
    * Removes all cells containing the letters of a detected word
    * (both in the board model and in the UI immediately).
    */
    private void removeWord(String word, GameCell startCell, Direction dir) {
        if (word == null || word.isEmpty() || startCell == null || dir == null) return;

        boardBusy = true; // lock the loop

        Platform.runLater(() -> {
            List<StackPane> nodesToFlash = new ArrayList<>();
            GameCell scanCell = startCell;

            // Collect nodes to flash
            for (int i = 0; i < word.length() && scanCell != null && !scanCell.isEmpty(); i++) {
                LetterBlock block = scanCell.getBlock();
                if (block != null && block.getBlock() != null) {
                    StackPane node = block.getBlock().getBlock();
                    if (node != null) nodesToFlash.add(node);
                }

                // Move to next cell
                if (dir == Direction.RIGHT) {
                    scanCell = scanCell.getRight();
                } else if (dir == Direction.DOWN) {
                    scanCell = scanCell.getDown();
                } else {
                    break;
                }
            }

            // --- Flash animation ---
            for (StackPane node : nodesToFlash) {
                javafx.animation.FadeTransition flash =
                        new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), node);
                flash.setFromValue(1.0);
                flash.setToValue(0.2);
                flash.setAutoReverse(true);
                flash.setCycleCount(2);
                flash.play();
            }

            // --- Pause to wait for animation ---
            javafx.animation.PauseTransition pause =
                    new javafx.animation.PauseTransition(javafx.util.Duration.millis(400));

            pause.setOnFinished(e -> {
                // Now safely clear the word cells
                GameCell clearCell = startCell;
                for (int i = 0; i < word.length() && clearCell != null; i++) {
                    LetterBlock block = clearCell.getBlock();
                    if (block != null && block.getBlock() != null) {
                        StackPane node = block.getBlock().getBlock();
                        if (node != null) GameWindow.removeBlockNode(node);
                    }
                    clearCell.clear();

                    if (dir == Direction.RIGHT) {
                        clearCell = clearCell.getRight();
                    } else if (dir == Direction.DOWN) {
                        clearCell = clearCell.getDown();
                    }
                }

                GameWindow.refreshBoard();
                board.applyGravity();

                // Let gravity animation complete before resuming
                javafx.animation.PauseTransition unfreeze =
                        new javafx.animation.PauseTransition(javafx.util.Duration.millis(250));
                unfreeze.setOnFinished(ev -> boardBusy = false);
                unfreeze.play();
            });

            pause.play();
        });
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
