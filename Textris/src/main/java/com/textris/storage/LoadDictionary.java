package com.textris.storage;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages loading the dictionary into memory and performing searches
 * on that dictionary. Collaborator for Dictionary class.
 *
 * Responsibilities:
 * - Loads in word list text file into a String[] array when Dictionary instantiates.
 * - Performs binary search on the array when Dictionary needs to check a possible word.
 *
 * @author Jason Watts
 */
public class LoadDictionary {
    private static String[] words = null;
    private static int listSize = 0;
    private static boolean loaded = false; // ✅ Prevents multiple redundant loads

    /**
     * Loads word list file Twordlist.txt into memory for the duration of the game.
     *
     * @throws IOException if file cannot be read
     */
    public static void load() throws IOException {
        // ✅ Prevent multiple reloads if dictionary already loaded
        if (loaded && words != null && listSize > 0) {
            System.out.println("Dictionary already loaded (" + listSize + " words)\n");
            return;
        }

        // ✅ Reset counters before each load
        listSize = 0;

        ArrayList<String> wordsTemp = new ArrayList<>();

        InputStream inFile = LoadDictionary.class.getResourceAsStream("/Twordlist.txt");
        if (inFile == null) {
            throw new IOException("Twordlist.txt not found in resources folder!");
        }

        BufferedReader readFile = new BufferedReader(new java.io.InputStreamReader(inFile));

        // Load each line of the word list into a temporary ArrayList
        String line = readFile.readLine();
        while (line != null) {
            wordsTemp.add(line.trim());
            listSize++;
            line = readFile.readLine();
        }

        // Convert ArrayList into a less mutable array
        words = wordsTemp.toArray(new String[0]);

        readFile.close();
        inFile.close();

        loaded = true; // ✅ Mark dictionary as loaded

        System.out.println("Dictionary loaded.\n");
        System.out.println("Dictionary size: " + listSize + " words\n");
    }

    /**
     * Performs binary search on String[] array words for a given string.
     *
     * @param wordToSearch The word to search for in the dictionary.
     * @return true if word is in dictionary; false otherwise.
     */
    public static boolean findWord(String wordToSearch) {
        if (words == null || listSize == 0) return false;

        int start = 0;
        int end = listSize - 1;
        int mid;

        while (start <= end) {
            mid = start + (end - start) / 2;

            int cmp = words[mid].compareTo(wordToSearch);
            if (cmp == 0) {
                return true;
            } else if (cmp < 0) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        return false;
    }
}
