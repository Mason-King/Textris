package com.textris.model;

import com.textris.model.GameCell;
import java.util.ArrayList;
import java.util.List;

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
 */

public class GameBoard {

    // Instantiate enough GameCells to make a board of the
    // size that we want + 1 row for overflow
    private final int width;
    private final int height;

    // Create a 2D arraylist to store the grid in
    private List<List<GameCell>> grid = new ArrayList<>();

     /**
     * Creates an empty GameBoard by interconnecting GameCells
     *
     * @param width how wide the board needs to be
     * @param height how tall the board needs to be
     */
    public GameBoard(int width, int height) {
        // TODO: initialize the right number of gamecells for this board
    }

    /**
     * Scans board and turns letterblocks into strings that will then be used to 
     * search for strings
     * @return new ArrayList of strings/possible words
     */
    public List<String> detectWords() {
        // TODO: use some method to get all updated strings
        // and parse them into different sections to find
        // possible words
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