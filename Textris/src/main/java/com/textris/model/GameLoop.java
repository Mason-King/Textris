package com.textris.model;

import model.GameBoard;
import model.Dictionary;

/**
 * Handles the state of the game
 *
 * Responsibilities:
 * - Controls main game loop
 * - Manages game state
 *
 * Collaborators:
 * - Game Board
 */

public class GameLoop {

    // Create a new gameboard of the correct size
    private GameBoard board;
    private Dictionary dictionary;
    private LetterBlock current;
    private int score;

    /**
     * Creates and manages the different aspects of the game in relation to one another
     *
     * @param board the actual grid of cells
     * @param dictionary the instance of the dictionary
     */
    public GameLoop(GameBoard board, Dictionary dictionary) {
        // TODO: initialize fields
    }
    
    public dropBlock() {

    }

    public spawnBlock() {
        
    }

}
