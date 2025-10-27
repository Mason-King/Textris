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

import com.textris.model.GameCell;
import java.util.ArrayList;
import java.util.List;

public class GameBoard {

 private final int rows; // add 1 row for checking if game should end (if any cell in row 8 is filled & the block is locked, then the game ends)
    private final int cols;

    // Create a 2D Array to store the grid in
    private final GameCell[][] grid;

     /**
     * Creates an empty GameBoard by interconnecting GameCells
     */
    public GameBoard() 
    {
        this.rows = 8;
        this.cols = 5;
        grid = new GameCell[this.rows][this.cols];
        initializeGrid();
    }

    /**
     * Initializes each GameCell in the empty GameBoard
     */ 
    private void initializeGrid()
    {
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                grid[i][j] = new GameCell();
            }
        }

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                GameCell cell = grid[i][j];
                if (i > 0 && i < (rows - 1))
                {
                    cell.setUp(grid[i-1][j]);
                    cell.setDown(grid[i+1][j]);
                }
                else if (i == 0)
                {
                    cell.setUp(null);
                    cell.setDown(grid[i+1][j]);
                }
                else if (i == (rows - 1))
                {
                    cell.setUp(grid[i-1][j]);
                    cell.setDown(null);
                }

                if (j > 0 && j < (cols - 1))
                {
                    cell.setLeft(grid[i][j-1]);
                    cell.setRight(grid[i][j+1]);
                }
                else if (j == 0)
                {
                    cell.setLeft(null);
                    cell.setRight(grid[i][j+1]);
                }
                else if (j == (cols - 1))
                {
                    cell.setLeft(grid[i][j-1]);
                    cell.setRight(null);
                }
            }
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
     * Scans horizontal and vertical rows containing the last placed letter block for
     * strings 3-5 letters long for referencing in Dictionary.
     * 
     * @param newBlock, the most recently placed block on the game board
     * @return ArrayList of strings containing possible words
     */
    public List<String> detectWords(GameCell newBlock) {
        List<Character> rowLetters = new ArrayList<>();
        List<Character> colLetters = new ArrayList<>();
        
        // Left-right bounds for horizontal searching
        GameCell leftCell = newBlock;
        GameCell rightCell = newBlock;
        
        /* Horizontal word finding */
        
        // Finding the left boundry for cells with letters from newBlock:
        // If the next left cell is not out of bounds and it is populated, go to it.
        while(leftCell.getLeft() != null && leftCell.getLeft().isEmpty() == false){
            leftCell = leftCell.getLeft();
        }

        // Finding the right boundry for cells with letters from newBlock:
        // If the next right cell is not out of bounds and it is populated, go to it
        while(rightCell.getRight() != null && rightCell.getRight().isEmpty() == false){
            rightCell = rightCell.getRight();
        }
        
        GameCell curCell = leftCell;
        boolean atEnd = false;
        
        // Aggregate all letters within these bounds
        while(atEnd == false){
            if(curCell == rightCell) atEnd = true;
            
            rowLetters.add(curCell.getBlock().getLetter());
            if(atEnd == false) curCell = curCell.getRight(); 
        }
        
        /* Vertical word finding */
        // Aggregate all letters descending from new block to the bottom of the board
        curCell = newBlock;
        
        while(curCell != null && curCell.isEmpty() == false){
            colLetters.add(curCell.getBlock().getLetter());
            curCell = curCell.getDown();
        }
        
        List<String> strings = new ArrayList<>();
        
        // Concatenate character arrays of valid length into strings and pass into String ArrayList
        
        int rSize = rowLetters.size();
        int cSize = colLetters.size();
        
        System.out.println("\nhoriz word len: " + rSize + ", vertical word len: " + cSize);
        
        if(rSize >= 3 && rSize <= 5){
            String rowWord = "";
            for (char c: rowLetters) rowWord += c;
            strings.add(rowWord);
        }
        
        if(cSize >= 3 && cSize <= 5){
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
    public void placeBlock(LetterBlock block) {
        // TODO: spawn block in upper leftmost gamecell
    }
}
