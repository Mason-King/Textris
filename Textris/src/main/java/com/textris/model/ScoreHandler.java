package com.textris.model;

/**
 * This class handles the user's score during an instance of the game.
 *
 * Responsibilities:
 * - Calculate and store the user's score
 * Collaborators:
 * - GameLoop
 * - 
 */
public class ScoreHandler 
{
    private int score;

    /**
     * On creation of a ScoreHandler, initializes the score to 0
     */
    public ScoreHandler()
    {
        this.score = 0;
    }

    /**
     * Returns the value of the current score
     *
     * @return the current score
     */
    public int getScore()
    {
        return this.score;
    }

    /**
     * Adds the bonus to the current score
     *
     * @param bonus the amount to add to the score
     */
    public void addScore(int bonus)
    {
        this.score += bonus;
    }

    
}
