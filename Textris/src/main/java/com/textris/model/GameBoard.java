package com.textris.model;

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
    private boolean isBoardBusy = false;

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
        return isBoardBusy;
    }

    /**
     * Represents a found word on the board and its starting position/direction.
     */
    public static class WordMatch 
    {
        /**
        * The found word.
        */
        public final String word;

        /**
        * The starting cell's position.
        */
        public final GameCell startCell;

        /**
        * The direction of the word.
        */
        public final Direction dir;

        /**
         * Constructs a WordMatch record.
         *
         * @param word the matched word
         * @param startCell the cell containing the first letter
         * @param dir the direction of the word (horizontal or vertical)
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
     * Gets the total number of rows in the board.
     *
     * @return number of rows
     */
    public int getRowCount() 
    {
        return this.rows;
    }

    /**
     * Gets the total number of columns in the board.
     *
     * @return number of columns
     */
    public int getColCount() 
    {
        return this.cols;
    }

    /**
     * Returns the GameCell stored at a specific grid coordinate.
     *
     * @param x the column index
     * @param y the row index
     * @return the GameCell at that position
     */
    public GameCell getCell(int x, int y) 
    {
        return grid[x][y];
    }
    
    /**
     * Detects horizontal and vertical words (3–5 letters) formed around the given cell.
     *
     * @param startCell the most recently placed cell
     * @return list of detected WordMatch objects
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
     * @param block the block to be placed
     * @return true if the block was placed successfully, false otherwise
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
        isBoardBusy = true;
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
        isBoardBusy = false;
    }

    /**
     * Links this GameBoard with the InputHandler for player input.
     *
     * @param inputHandler the InputHandler to associate
     */
    public void setInputHandler(InputHandler inputHandler) 
    {
        this.inputHandler = inputHandler;
    }

    /**
     * Prints the current board state to the console for debugging.
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

