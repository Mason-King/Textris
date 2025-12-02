/**
 * Handles the state of the game.
 *
 * Responsibilities:
 * - Controls main game loop
 * - Manages game state
 *
 * Collaborators:
 * - GameBoard
 * - Dictionary
 *
 * @author Cruz Shafer, Carrie Rochell
 */
package com.textris.model;

import com.textris.media.Block;
import com.textris.ui.GameWindow;
import com.textris.ui.InputHandler;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates and manages the game
 * This includes spawning, moving, and locking blocks, as well as detecting words
 * and updating the score.
 */
public class GameLoop {
    private GameBoard board;
    private Dictionary dictionary;
    private LetterBlock current;
    private LetterBlock previous;
    private int score;
    private boolean gameOver;

    private Block fallingBlock;
    private InputHandler inputHandler;
    private boolean boardBusy = false;
    private boolean gameOn;
    private final java.util.concurrent.atomic.AtomicInteger pendingClears =
        new java.util.concurrent.atomic.AtomicInteger(0);


    /**
     * Constructs a GameLoop instance that controls game progression.
     *
     * @param inputHandler handles user input
     * @param board the grid of cells where blocks are placed
     * @param dictionary dictionary used for validating formed words
     */
    public GameLoop(InputHandler inputHandler, GameBoard board, Dictionary dictionary) {
        this.board = board;
        this.dictionary = dictionary;
        this.previous = null;
        this.score = 0;
        this.gameOver = false;
        this.inputHandler = inputHandler;
        this.board.setInputHandler(inputHandler);
    }

    /**
     * Called each tick to update the screen and move blocks.
     * Handles both falling block movement and block placement.
     */
    public void tick() {
        if (gameOver || boardBusy) return;

        if (current == null) {
            dropBlock();
            return;
        }

        if (board.canMove(current, Direction.DOWN)) {
            board.move(current, Direction.DOWN);
        } else {
            setBlock();
        }
    }

    /**
     * Generates a new falling block at the top of the board.
     * Also checks for game over conditions if a new block cannot be placed.
     */
    public void dropBlock() {
        if (boardBusy) return;

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

            GameWindow.showGameOverOverlay(() -> {
                Platform.runLater(() -> {
                    reset();
                    start();
                });
            });
        }
    }

    /**
     * Locks a block in place once it reaches the bottom of the board or another block.
     * Triggers word detection and gravity application.
     */
    public void setBlock() {
        previous = current;
        current = null;

        boolean wordsFound = findWords();

        if (!wordsFound) {
            applyGravity();
            dropBlock(); 
        }
    }

    /**
     * Applies gravity to cause any floating blocks to fall to the lowest empty space.
     */
    public void applyGravity() {
        board.applyGravity();
    }

    /**
     * Detects any valid words formed by the most recently placed block.
     * Awards points and removes matched words from the board.
     * 
     * @return true if a word was found
     */
    public boolean findWords() {
        if (previous == null) return false;

        GameCell cell = board.getCell(previous.getCol(), previous.getRow());
        List<GameBoard.WordMatch> matches = board.detectWords(cell);

        if (matches.isEmpty()) return false;

        pendingClears.set(matches.size());

        for (GameBoard.WordMatch match : matches) {
            System.out.println("Found word = " + match.word);
            removeWord(match.word, match.startCell, match.dir);
            addToScore(match.word.length());
        }

        return true;
    }


    /**
     * Removes a detected word from the board, clears associated blocks,
     * and plays a short fade animation.
     *
     * @param word the detected word
     * @param startCell the starting cell of the word
     * @param dir the direction of the word (horizontal or vertical)
     */
    private void removeWord(String word, GameCell startCell, Direction dir) {
        if (word == null || word.isEmpty() || startCell == null || dir == null) return;
        boardBusy = true;

        Platform.runLater(() -> {
            List<StackPane> nodesToFlash = new ArrayList<>();
            GameCell scanCell = startCell;

            for (int i = 0; i < word.length() && scanCell != null && !scanCell.isEmpty(); i++) {
                LetterBlock block = scanCell.getBlock();
                if (block != null && block.getBlock() != null) {
                    StackPane node = block.getBlock().getBlock();
                    if (node != null) nodesToFlash.add(node);
                }

                if (dir == Direction.RIGHT) scanCell = scanCell.getRight();
                else if (dir == Direction.DOWN) scanCell = scanCell.getDown();
            }

            // Flash effect for cleared blocks
            for (StackPane node : nodesToFlash) {
                javafx.animation.FadeTransition flash =
                        new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), node);
                flash.setFromValue(1.0);
                flash.setToValue(0.2);
                flash.setAutoReverse(true);
                flash.setCycleCount(2);
                flash.play();
            }

            javafx.animation.PauseTransition pause =
                    new javafx.animation.PauseTransition(javafx.util.Duration.millis(400));

            pause.setOnFinished(e -> {
                GameCell clearCell = startCell;
                for (int i = 0; i < word.length() && clearCell != null; i++) {
                    LetterBlock block = clearCell.getBlock();
                    if (block != null && block.getBlock() != null) {
                        StackPane node = block.getBlock().getBlock();
                        if (node != null) GameWindow.removeBlockNode(node);
                    }
                    clearCell.clear();

                    if (dir == Direction.RIGHT) clearCell = clearCell.getRight();
                    else if (dir == Direction.DOWN) clearCell = clearCell.getDown();
                }

                GameWindow.refreshBoard();
                board.applyGravity();

                javafx.animation.PauseTransition unfreeze =
                        new javafx.animation.PauseTransition(javafx.util.Duration.millis(250));
                unfreeze.setOnFinished(ev -> {
                    int remaining = pendingClears.decrementAndGet();
                    if (remaining <= 0) {
                        boardBusy = false;
                        dropBlock();
                    } else {
                        System.out.println("Remaining clears: " + remaining);
                    }
                });

                unfreeze.play();
            });

            pause.play();
        });
    }

    /**
     * Returns the current player score.
     * @return total score
     */
    public int getScore() {
        return score;
    }

    /**
     * Adds points for cleared words and updates the score display.
     * @param wordLength length of the cleared word
     */
    public void addToScore(int wordLength) {
        int points = wordLength * 10;
        score += points;
        GameWindow.updateScore(score);
    }

    /**
     * Checks whether the game has ended.
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Resets the game state, clearing the board and score.
     */
    public void reset() {
        this.score = 0;
        GameWindow.updateScore(0);
        this.current = null;
        this.previous = null;
        this.gameOver = false;
        this.boardBusy = false;

        board.clearBoard();
        GameWindow.clearBoardUI();

        System.out.println("Game restarted!");
    }

    /**
     * Starts the main game loop
     * Continues running until the game ends.
     */
    public void start() {
        new Thread(() -> {
            while (!this.isGameOver()) {
                this.tick();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
}

