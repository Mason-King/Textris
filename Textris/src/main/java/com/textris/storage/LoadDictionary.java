package com.textris.storage;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// findWord(String str) //does a binary search for word

public class LoadDictionary {
    private static String[] words = null;
    private static int listSize = 0;
    
    /**
     * Loads word list file Twordlist_curated.txt into memory for the duration of the game.
     * 
     * @throws IOException
     */
    public static void load() throws java.io.IOException {
        ArrayList<String> wordsTemp = new ArrayList<>();
        
        InputStream inFile = LoadDictionary.class.getResourceAsStream("/Twordlist_curated.txt");
        BufferedReader readFile = new BufferedReader(new java.io.InputStreamReader(inFile));
        
        // checking for end of file
        String line = readFile.readLine();
        
        while (line != null) {
            wordsTemp.add(line);
            listSize++;
            line = readFile.readLine();
        }
        
        words = (String[]) wordsTemp.toArray(new String[0]);
      
        readFile.close();
        inFile.close();
        
        Arrays.sort(words);
        
        System.out.println("Dictionary loaded.\n");
        System.out.println("Dictionary size: " + listSize + " words\n");
    }
    
    public static boolean findWord(String wordToSearch){
        int start = 0;
        int end = (listSize - 1);
        
        int mid = 0;
        
        while (start <= end){
            mid = start + (end - start)/2;
            
            System.out.println(start + " " + mid + " " + end);
            
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
        //return binarySearch(str);
    }
}