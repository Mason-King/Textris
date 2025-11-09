package com.textris.storage;

/**
 * Represents a single player's score entry in the game.
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
