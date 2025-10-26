package com.textris.data;

/**
 * Represents a single player's score entry in the game.
 * <p>
 * A {@code ScoreEntry} object stores the player's name and their corresponding
 * score value. Instances of this class are typically used by the
 * {@link com.textris.data.ScoreManager} to maintain and display high scores
 * in the scoreboard.
 * </p>
 *
 */
public class ScoreEntry {

    /** The name of the player. */
    private String name;

    /** The player's score. */
    private int score;

    /**
     * Constructs a new {@code ScoreEntry} with the specified player name and score.
     *
     * @param name  the name of the player
     * @param score the player's score value
     */
    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the score value associated with this player.
     *
     * @return the player's score
     */
    public int getScore() {
        return score;
    }
}
