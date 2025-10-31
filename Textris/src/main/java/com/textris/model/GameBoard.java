/**
 * This class represents the main game board.
 *
 * Responsibilities:
 * - Create and link the appropriate number of GameCells in a 
 *    board-like fashion
 * - Manage those GameCells
 *
 * Collaborators:
 * - LetterBlock
 * - GameCell
 * 
 * @authors Cruz Shafer, Jason Watts
 */
package com.textris.model;

import java.util.List;

import com.textris.model.GameCell;
import com.textris.ui.GameWindow;
import com.textris.ui.InputHandler;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameBoard 
{
    private final Dictionary dictionary = new Dictionary();
    private final int cols;
    private final int rows; // add 1 row for checking if game should end (if any cell in row 8 is filled & the block is locked, then the game ends)

    // Create a 2D Array to store the grid in
    private final GameCell[][] grid;

    private InputHandler inputHandler;

     /**
     * Creates an empty GameBoard by interconnecting GameCells.
     */
    public GameBoard()
    {
        this.cols = 5;
        this.rows = 8;
        grid = new GameCell[this.cols][this.rows];
        initializeGrid();
    }
    
    private boolean isBoardBusy = false;

    public boolean isBoardBusy() {
        return isBoardBusy;
    }
    
    public static class WordMatch {
        public final String word;
        public final GameCell startCell; // the cell containing the first letter of the matched substring
        public final Direction dir;      // Direction.RIGHT for horizontal, Direction.DOWN for vertical

        public WordMatch(String word, GameCell startCell, Direction dir) {
            this.word = word;
            this.startCell = startCell;
            this.dir = dir;
        }
    }

    /**
     * Initializes each GameCell in the empty GameBoard
     */ 
    private void initializeGrid()
    {
        for (int i = 0; i < cols; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                grid[i][j] = new GameCell();
            }
        }

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                GameCell cell = grid[i][j];
                cell.setLeft(i > 0 ? grid[i - 1][j] : null);
                cell.setRight(i < cols - 1 ? grid[i + 1][j] : null);
                cell.setUp(j > 0 ? grid[i][j - 1] : null);
                cell.setDown(j < rows - 1 ? grid[i][j + 1] : null);
            }
        }
    }
    
    public void clearBoard() {
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                grid[col][row].clear();
            }
        }
    }

    /**
     * Moves a LetterBlock in the given direction if possible.
     * Updates the GameCell grid, and the blocks row and column.
     *
     * @param block - LetterBlock to move
     * @param dir - Direction to move the block (LEFT, RIGHT, DOWN)
     */
    public void move(LetterBlock block, Direction dir) {
        int row = block.getRow();
        int col = block.getCol();

        // Clear the current cell
        grid[col][row].clear();

        // Calculate new row/col based on direction
        int newRow = row;
        int newCol = col;

        switch (dir) {
            case DOWN -> newRow++;
            case LEFT -> newCol--;
            case RIGHT -> newCol++;
        }

        // Place the block in the new cell
        grid[newCol][newRow].setBlock(block);
        block.setRow(newRow);
        block.setCol(newCol);

        inputHandler.updateActiveCell(block);

        //Define final since lambda
        final LetterBlock fb = block;

        // Update the visual node in the UI
        Platform.runLater(() -> {
            var node = fb.getBlock().getBlock();
            node.setLayoutX(fb.getCol() * GameWindow.SIZE); // col → X
            node.setLayoutY(fb.getRow() * GameWindow.SIZE); // row → Y
        });

    }

    /**
     * Checks if a LetterBlock can move in a specific direction
     *
     * @param block - LetterBlock to check
     * @param dir - Direction to check (LEFT, RIGHT, DOWN)
     * @return - boolean if the block can move
     */
    public boolean canMove(LetterBlock block, Direction dir) {
        int row = block.getRow();
        int col = block.getCol();

        switch (dir) {
            case DOWN:
                if (row == rows - 1) return false;
                return grid[col][row + 1].isEmpty();
            case LEFT:
                if (col == 0) return false;
                return grid[col - 1][row].isEmpty();
            case RIGHT:
                if (col == cols - 1) return false;
                return grid[col + 1][row].isEmpty();
            default:
                return false;
        }
    }

    /**
     * Get the number of rows in the GameBoard
     * 
     * @return number of rows
     */
    public int getRowCount()
    {
        return this.rows;
    }

    /**
     * Get the number of columns in the GameBoard
     * 
     * @return number of columns
     */
    public int getColCount()
    {
        return this.cols;
    }


    /**
     * Returns the cell stored at a specific place in the grid
     *
     * @param x column of block (starting @ 0)
     * @param y row of block (starting @ 0)
     * @return the cell itself
     */
    public GameCell getCell(int x, int y)
    {
        return grid[x][y];
    }

     /**
     * Scans horizontal and vertical rows containing the last placed letter block for
     * strings 3-5 letters long for referencing in Dictionary.
     * 
     * @param startCell, the most recently placed block on the game board
     * @return ArrayList of strings containing possible words
     */
    public List<WordMatch> detectWords(GameCell startCell) {
        List<WordMatch> matches = new ArrayList<>();
        if (startCell == null || startCell.isEmpty()) return matches;

        // --- HORIZONTAL ---
        // find leftmost cell in the contiguous run
        GameCell left = startCell;
        while (left.getLeft() != null && !left.getLeft().isEmpty()) left = left.getLeft();

        // collect cells left->right
        List<GameCell> horizCells = new ArrayList<>();
        GameCell cur = left;
        while (cur != null && !cur.isEmpty()) {
            horizCells.add(cur);
            cur = cur.getRight();
        }

        // convert to letters string
        int hLen = horizCells.size();
        if (hLen >= 3) {
            // for every substring length 3..5
            for (int len = 3; len <= 5; len++) {
                if (len > hLen) break;
                // slide window over horizontal cells
                for (int startIdx = 0; startIdx <= hLen - len; startIdx++) {
                    StringBuilder sb = new StringBuilder(len);
                    for (int k = 0; k < len; k++) sb.append(horizCells.get(startIdx + k).getBlock().getLetter());
                    String candidate = sb.toString().toLowerCase();
                    if (dictionary.isValid(candidate)) {
                        // start cell for this substring:
                        GameCell matchStartCell = horizCells.get(startIdx);
                        matches.add(new WordMatch(candidate, matchStartCell, Direction.RIGHT));
                    }
                }
            }
        }

        // --- VERTICAL ---
        GameCell top = startCell;
        while (top.getUp() != null && !top.getUp().isEmpty()) top = top.getUp();

        List<GameCell> vertCells = new ArrayList<>();
        cur = top;
        while (cur != null && !cur.isEmpty()) {
            vertCells.add(cur);
            cur = cur.getDown();
        }

        int vLen = vertCells.size();
        if (vLen >= 3) {
            for (int len = 3; len <= 5; len++) {
                if (len > vLen) break;
                for (int startIdx = 0; startIdx <= vLen - len; startIdx++) {
                    StringBuilder sb = new StringBuilder(len);
                    for (int k = 0; k < len; k++) sb.append(vertCells.get(startIdx + k).getBlock().getLetter());
                    String candidate = sb.toString().toLowerCase();
                    if (dictionary.isValid(candidate)) {
                        GameCell matchStartCell = vertCells.get(startIdx);
                        matches.add(new WordMatch(candidate, matchStartCell, Direction.DOWN));
                    }
                }
            }
        }

        return matches;
    }
    
    /**
     * Places a letterBlock in the starting gamecell
     *
     * @param block to be placed
     */
    public boolean placeBlock(LetterBlock block) {
        int row = block.getRow();
        int col = block.getCol();

        if (row < 0 || row >= this.rows || col < 0 || col >= this.cols) {
            return false;
        }

        GameCell cell = grid[col][row];

        if(!cell.isEmpty()) {
            return false;
        }

        cell.setBlock(block);
        return true;
    }

    public void applyGravity() {
        isBoardBusy = true;
        for (int row = rows - 2; row >= 0; row--) {
            for (int col = 0; col < cols; col++) {
                GameCell cur = grid[col][row];
                if (!cur.isEmpty()) {
                    LetterBlock block = cur.getBlock();
                    while (canMove(block, Direction.DOWN)) {
                        move(block, Direction.DOWN);
                    }
                }
            }
        }
        isBoardBusy = false;
    }

    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    public void printBoard() {
        System.out.println("---- BOARD ----");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                GameCell cell = grid[j][i];
                if (cell.isEmpty() || cell.getBlock() == null) {
                    System.out.print("* ");
                } else {
                    try {
                        System.out.print(cell.getBlock().getLetter() + " ");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.print("? "); // placeholder for debugging
                        e.printStackTrace();
                    }
                }

            }
            System.out.println();
        }
        System.out.println("---------------");
    }


}
