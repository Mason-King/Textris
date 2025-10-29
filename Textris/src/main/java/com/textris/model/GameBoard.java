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

public class GameBoard 
{
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

        for (int i = 0; i < cols; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                GameCell cell = grid[i][j];
                if (i > 0 && i < (cols - 1))
                {
                    cell.setLeft(grid[i-1][j]);
                    cell.setRight(grid[i+1][j]);
                }
                else if (i == 0)
                {
                    cell.setLeft(null);
                    cell.setRight(grid[i+1][j]);
                }
                else if (i == (cols - 1))
                {
                    cell.setLeft(grid[i-1][j]);
                    cell.setRight(null);
                }

                if (j > 0 && j < (rows - 1))
                {
                    cell.setUp(grid[i][j-1]);
                    cell.setDown(grid[i][j+1]);
                }
                else if (j == 0)
                {
                    cell.setUp(null);
                    cell.setDown(grid[i][j+1]);
                }
                else if (j == (rows - 1))
                {
                    cell.setUp(grid[i][j-1]);
                    cell.setDown(null);
                }
            }
        }
    }

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
        final int fx = newRow, fy = newCol;
        final LetterBlock fb = block;

        // Update the visual node in the UI
        Platform.runLater(() -> {
            var node = fb.getBlock().getBlock();
            node.setLayoutX(fb.getCol() * GameWindow.SIZE); // col → X
            node.setLayoutY(fb.getRow() * GameWindow.SIZE); // row → Y
        });

    }

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
     * @param newBlock, the most recently placed block on the game board
     * @return ArrayList of strings containing possible words
     */
    public List<String> detectWords(GameCell newBlock) 
    {
        List<Character> rowLetters = new ArrayList<>();
        List<Character> colLetters = new ArrayList<>();
        
        // Left-right bounds for horizontal searching
        GameCell leftCell = newBlock;
        GameCell rightCell = newBlock;
        
        /* Horizontal word finding */
        
        // Finding the left boundry for cells with letters from newBlock:
        // If the next left cell is not out of bounds and it is populated, go to it.
        while (leftCell.getLeft() != null && leftCell.getLeft().isEmpty() == false)
        {
            leftCell = leftCell.getLeft();
        }

        // Finding the right boundry for cells with letters from newBlock:
        // If the next right cell is not out of bounds and it is populated, go to it
        while (rightCell.getRight() != null && rightCell.getRight().isEmpty() == false)
        {
            rightCell = rightCell.getRight();
        }
        
        GameCell curCell = leftCell;
        boolean atEnd = false;
        
        // Aggregate all letters within these bounds
        while (atEnd == false)
        {
            if(curCell == rightCell) atEnd = true;
            
            rowLetters.add(curCell.getBlock().getLetter());
            if(atEnd == false) curCell = curCell.getRight(); 
        }
        
        /* Vertical word finding */
        // Aggregate all letters descending from new block to the bottom of the board
        curCell = newBlock;
        
        while (curCell != null && curCell.isEmpty() == false)
        {
            colLetters.add(curCell.getBlock().getLetter());
            curCell = curCell.getDown();
        }
        
        List<String> strings = new ArrayList<>();
        
        // Concatenate character arrays of valid length into strings and pass into String ArrayList
        
        int rSize = rowLetters.size();
        int cSize = colLetters.size();
        
        System.out.println("\nhoriz word len: " + rSize + ", vertical word len: " + cSize);
        
        if (rSize >= 3 && rSize <= 5)
        {
            String rowWord = "";
            for (char c: rowLetters) rowWord += c;
            strings.add(rowWord);
        }
        
        if (cSize >= 3 && cSize <= 5)
        {
            String colWord = "";
            for (char c: colLetters) colWord += c;
            strings.add(colWord);
        }
        
        return strings;
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
        Platform.runLater(() -> {
            for (int col = 0; col < this.cols; col++) {
                for (int row = this.rows - 2; row >= 0; row--) {
                    GameCell cur = grid[col][row];
                    if (!cur.isEmpty()) {
                        int dropRow = row;
                        while (dropRow + 1 < rows && grid[col][dropRow + 1].isEmpty()) {
                            dropRow++;
                        }
                        if (dropRow != row) {
                            LetterBlock block = cur.getBlock();
                            cur.clear();
                            grid[col][dropRow].setBlock(block);
                            block.setRow(dropRow);

                            // Instant move
                            block.getBlock().getBlock().setLayoutY(dropRow * GameWindow.SIZE);
                        }
                    }
                }
            }
        });
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
