package com.textris.model;

import static com.textris.model.Direction.DOWN;
import static com.textris.model.Direction.LEFT;
import static com.textris.model.Direction.RIGHT;
import java.util.List;
import com.textris.ui.GameWindow;
import com.textris.ui.InputHandler;
import javafx.application.Platform;
import java.util.ArrayList;

/**
 * This class represents the main game board.
 *
 * Responsibilities:
 * - Create and link the appropriate number of GameCells in a 
 *   board-like fashion
 * - Manage those GameCells
 *
 * Collaborators:
 * - LetterBlock
 * - GameCell
 * 
 */
public class GameBoard 
{
    private final Dictionary dictionary = new Dictionary();
    private final int cols;
    private final int rows; // add 1 row for checking if game should end
    private final GameCell[][] grid;
    private InputHandler inputHandler;

    // renamed isBoardBusy -> boardBusy for consistency with GameLoop
    private boolean boardBusy = false;

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
     * Returns whether the board is currently busy (processing a movement or gravity).
     *
     * @return true if the board is busy, false otherwise
     */
    public boolean isBoardBusy() 
    {
        return boardBusy;
    }

    /**
     * Represents a found word on the board and its starting position/direction.
     */
    public static class WordMatch 
    {
        /**
        * The word itself.
        */
        public final String word;
        
        /**
        * The GameCell that the word starts in.
        */
        public final GameCell startCell;
        
        /**
        * The direction that the word goes in.
        */
        public final Direction dir;

    
        /**
        * Checks if a LetterBlock can move in a given direction.
        *
        * @param word the word
        * @param startCell the cell that it starts in
        * @param dir the direction of the word
        */
        public WordMatch(String word, GameCell startCell, Direction dir) 
        {
            this.word = word;
            this.startCell = startCell;
            this.dir = dir;
        }
    }

    /**
     * Initializes each GameCell in the grid and links its neighbors.
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
                cell.setLeft(i > 0 ? grid[i - 1][j] : null);
                cell.setRight(i < cols - 1 ? grid[i + 1][j] : null);
                cell.setUp(j > 0 ? grid[i][j - 1] : null);
                cell.setDown(j < rows - 1 ? grid[i][j + 1] : null);
            }
        }
    }

    /**
     * Clears all cells on the board, resetting it to empty.
     */
    public void clearBoard() 
    {
        for (int col = 0; col < cols; col++) 
        {
            for (int row = 0; row < rows; row++) 
            {
                grid[col][row].clear();
            }
        }
    }

    /**
     * Moves a LetterBlock in the given direction if possible.
     * Updates the GameCell grid, and the block’s row and column.
     *
     * @param block the LetterBlock to move
     * @param dir the direction to move (LEFT, RIGHT, DOWN)
     */
    public void move(LetterBlock block, Direction dir) 
    {
        int row = block.getRow();
        int col = block.getCol();
        grid[col][row].clear();

        int newRow = row;
        int newCol = col;

        switch (dir) 
        {
            case DOWN -> newRow++;
            case LEFT -> newCol--;
            case RIGHT -> newCol++;
        }

        grid[newCol][newRow].setBlock(block);
        block.setRow(newRow);
        block.setCol(newCol);

        inputHandler.updateActiveCell(block);
        final LetterBlock fb = block;

        Platform.runLater(() -> 
        {
            var node = fb.getBlock().getBlock();
            node.setLayoutX(fb.getCol() * GameWindow.SIZE);
            node.setLayoutY(fb.getRow() * GameWindow.SIZE);
        });
    }

    /**
     * Checks if a LetterBlock can move in a given direction.
     *
     * @param block the LetterBlock to check
     * @param dir the direction to test
     * @return true if the block can move, false otherwise
     */
    public boolean canMove(LetterBlock block, Direction dir) 
    {
        int row = block.getRow();
        int col = block.getCol();

        switch (dir) 
        {
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
     * Returns the number of rows in the board.
     *
     * @return the number of rows in the board
     */
    public int getRowCount() 
    { 
        return this.rows; 
    }
    
    
    /**
     * Returns the number of columns in the board.
     *
     * @return the number of columns in the board
     */
    public int getColCount() 
    { 
        return this.cols; 
    }
    
    
    /**
     * Returns the GameCell that exists at a certain point in the board.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the GameCell that exists at x,y in the board
     */
    public GameCell getCell(int x, int y) 
    { 
        return grid[x][y]; 
    }

    
    /**
     * Detects horizontal and vertical words (3–5 letters) formed around the given cell.
     * 
     * @param startCell the GameCell to start the search from
     * @return the list of valid words
     */
    public List<WordMatch> detectWords(GameCell startCell) 
    {
        List<WordMatch> matches = new ArrayList<>();
        if (startCell == null || startCell.isEmpty()) return matches;

        // HORIZONTAL SCAN
        GameCell left = startCell;
        while (left.getLeft() != null && !left.getLeft().isEmpty()) left = left.getLeft();

        List<GameCell> horizCells = new ArrayList<>();
        GameCell cur = left;
        while (cur != null && !cur.isEmpty()) 
        {
            horizCells.add(cur);
            cur = cur.getRight();
        }

        int hLen = horizCells.size();
        if (hLen >= 3) 
        {
            for (int len = 3; len <= 5; len++) 
            {
                if (len > hLen) break;
                for (int startIdx = 0; startIdx <= hLen - len; startIdx++) 
                {
                    StringBuilder sb = new StringBuilder(len);
                    for (int k = 0; k < len; k++)
                        sb.append(horizCells.get(startIdx + k).getBlock().getLetter());
                    String candidate = sb.toString().toLowerCase();
                    if (dictionary.isValid(candidate)) 
                    {
                        GameCell matchStartCell = horizCells.get(startIdx);
                        matches.add(new WordMatch(candidate, matchStartCell, Direction.RIGHT));
                    }
                }
            }
        }

        // VERTICAL SCAN
        List<GameCell> vertCells = new ArrayList<>();
        cur = startCell;
        while (cur != null && !cur.isEmpty()) {
            vertCells.add(cur);
            cur = cur.getDown();
        }

        int vLen = vertCells.size();
        if (vLen >= 3) 
        {
            for (int len = 3; len <= 5; len++) 
            {
                if (len > vLen) break;
                for (int startIdx = 0; startIdx <= vLen - len; startIdx++) 
                {
                    StringBuilder sb = new StringBuilder(len);
                    for (int k = 0; k < len; k++)
                        sb.append(vertCells.get(startIdx + k).getBlock().getLetter());
                    String candidate = sb.toString().toLowerCase();
                    if (dictionary.isValid(candidate)) 
                    {
                        GameCell matchStartCell = vertCells.get(startIdx);
                        matches.add(new WordMatch(candidate, matchStartCell, Direction.DOWN));
                    }
                }
            }
        }

        return matches;
    }

    /**
     * Attempts to place a block in its designated starting cell.
     * 
     * @param block the block to place
     * @return true if the block was placed successfully
     */
    public boolean placeBlock(LetterBlock block) 
    {
        int row = block.getRow();
        int col = block.getCol();

        if (row < 0 || row >= this.rows || col < 0 || col >= this.cols) 
        {
            return false;
        }

        GameCell cell = grid[col][row];
        if (!cell.isEmpty()) 
        {
            return false;
        }

        cell.setBlock(block);
        return true;
    }

    /**
     * Applies gravity to all blocks, letting unsupported blocks fall downward.
     */
    public void applyGravity() 
    {
        boardBusy = true;

        for (int row = rows - 2; row >= 0; row--) 
        {
            for (int col = 0; col < cols; col++) 
            {
                GameCell cur = grid[col][row];
                if (!cur.isEmpty()) 
                {
                    LetterBlock block = cur.getBlock();
                    while (canMove(block, Direction.DOWN)) 
                    {
                        move(block, Direction.DOWN);
                    }
                }
            }
        }

        boardBusy = false;
    }

    
    /**
     * Sets the board's active input handler.
     *
     * @param inputHandler the input handler
     */
    public void setInputHandler(InputHandler inputHandler) 
    { this.inputHandler = inputHandler; }

    
    /**
     * Prints the board to the console for debugging.
     */
    public void printBoard() 
    {
        System.out.println("---- BOARD ----");
        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                GameCell cell = grid[j][i];
                if (cell.isEmpty() || cell.getBlock() == null) 
                {
                    System.out.print("* ");
                } 
                else 
                {
                    try 
                    {
                        System.out.print(cell.getBlock().getLetter() + " ");
                    } catch (ArrayIndexOutOfBoundsException e) 
                    {
                        System.out.print("? ");
                        e.printStackTrace();
                    }
                }
            }
            System.out.println();
        }
        System.out.println("---------------");
    }
}

