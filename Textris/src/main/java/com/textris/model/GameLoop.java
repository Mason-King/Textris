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
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;


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

    public GameLoop(InputHandler inputHandler, GameBoard board, Dictionary dictionary) {
        this.board = board;
        this.dictionary = dictionary;
        this.previous = null;
        this.score = 0;
        this.gameOver = false;
        this.inputHandler = inputHandler;
        this.board.setInputHandler(inputHandler);
    }

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
            dropBlock();
        }
    }

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

    public void setBlock() {
        findWords();
        previous = current;
        current = null;
        applyGravity();
    }

    public void applyGravity() {
        board.applyGravity();
    }

    public void findWords() {
        if (previous == null) return;

        GameCell cell = board.getCell(previous.getCol(), previous.getRow());
        List<GameBoard.WordMatch> matches = board.detectWords(cell);

        for (GameBoard.WordMatch match : matches) {
            System.out.println("Found word: " + match.word + " dir=" + match.dir);
            removeWord(match.word, match.startCell, match.dir);
            addToScore(match.word.length()); // award 10 points per letter
        }
    }

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
                unfreeze.setOnFinished(ev -> boardBusy = false);
                unfreeze.play();
            });

            pause.play();
        });
    }

    public int getScore() {
        return score;
    }

    /** Adds points for cleared words and updates the UI */
    public void addToScore(int wordLength) {
        int points = wordLength * 10;
        score += points;
        GameWindow.updateScore(score);
    }

    public boolean isGameOver() {
        return gameOver;
    }

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

