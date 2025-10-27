package com.textris.storage;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

/**
 * The {@code ScoreManager} class provides methods for managing player scores in the game.
 * Scores are stored in a JSON file located in a dedicated "Textris" folder inside the user's home directory.
 *
 * <p>All scores are persisted between application sessions.</p>
 *
 */
public class ScoreManager {

    /** Maximum number of top scores to keep. */
    private static final int MAX_SCORES = 5;

    /** Gson instance for reading/writing JSON data. */
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /** The directory where the scores.json file will be stored. */
    private static final Path SCORE_DIR = Paths.get(System.getProperty("user.home"), "Textris");

    /** The path to the JSON file that stores scores. */
    private static final Path SCORE_FILE = SCORE_DIR.resolve("scores.json");

    /**
     * Loads the list of saved scores from the scores.json file.
     * If the file does not exist, an empty list is returned.
     *
     * @return a list of {@link ScoreEntry} objects, sorted from highest to lowest score
     */
    public static List<ScoreEntry> loadScores() {
        try {
            if (!Files.exists(SCORE_FILE)) return new ArrayList<>();

            Reader reader = Files.newBufferedReader(SCORE_FILE);
            List<ScoreEntry> scores = gson.fromJson(reader, new TypeToken<List<ScoreEntry>>() {}.getType());
            reader.close();
            return scores != null ? scores : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Saves the provided list of {@link ScoreEntry} objects to disk.
     * Creates the directory if it does not exist.
     *
     * @param scores the list of scores to save
     */
    public static void saveScores(List<ScoreEntry> scores) {
        try {
            if (!Files.exists(SCORE_DIR)) {
                Files.createDirectories(SCORE_DIR);
            }

            Writer writer = Files.newBufferedWriter(SCORE_FILE);
            gson.toJson(scores, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new score entry and keeps only the top {@value #MAX_SCORES} scores.
     *
     * @param name  the player's name
     * @param score the player's score
     */
    public static void addScore(String name, int score) {
        List<ScoreEntry> scores = loadScores();
        scores.add(new ScoreEntry(name, score));

        // Sort scores descending
        scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        // Keep only top MAX_SCORES
        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }

        saveScores(scores);
    }
}
