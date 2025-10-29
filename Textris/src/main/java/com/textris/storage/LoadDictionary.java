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
 * - Loads in word list text file into String[] array when Dictionary instantiates.
 * - Performs binary search on the array when Dictionary needs to check a possible word.
 * 
 * @author Jason Watts
 */

public class LoadDictionary {
    private static String[] words = null;
    private static int listSize = 0;
    
    /**
     * Loads word list file Twordlist.txt into memory for the duration of the game.
     * 
     * @throws IOException
     */
    public static void load() throws java.io.IOException {
        ArrayList<String> wordsTemp = new ArrayList<>();
        
        InputStream inFile = LoadDictionary.class.getResourceAsStream("/Twordlist.txt");
        BufferedReader readFile = new BufferedReader(new java.io.InputStreamReader(inFile));
        
        // Load in each line of word list into temporary ArrayList
        String line = readFile.readLine();
        
        while (line != null) {
            wordsTemp.add(line);
            listSize++;
            line = readFile.readLine();
        }
        
        // Turn ArrayList into a less mutable array
        words = (String[]) wordsTemp.toArray(new String[0]);
      
        readFile.close();
        inFile.close();
        
        System.out.println("Dictionary loaded.\n");
        System.out.println("Dictionary size: " + listSize + " words\n");
    }
    
    /**
     * Performs binary search on String[] array words for a given string.
     * 
     * @param wordToSearch
     * @return true or false whether or not word is in dictionary
     */
    public static boolean findWord(String wordToSearch){
        int start = 0;
        int end = (listSize - 1);
        
        int mid = 0;
        
        while (start <= end){
            mid = start + (end - start)/2;
            
            if (words[mid].compareTo(wordToSearch) == 0){
                return true;
            }
            else if(words[mid].compareTo(wordToSearch) < 0){
                start = mid + 1;
            }
            else {
                end = mid - 1;
            }
        }
        return false;
    }
}