package com.textris.core;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Game {
    public void start() {
        System.out.println("Starting textris game...");
        // TODO: Implement game loop
        //String[] e = {};
        try {
            String[] words = makeWordList();
            
            for (String str : words) {
            System.out.println(str);
            }
        } catch (IOException e){
            System.out.println("Word list could not be read. Aborting...\n");
        }
        
    }
    
    private String[] makeWordList() throws IOException,java.io.FileNotFoundException {
        List wordList = new ArrayList<String>();
        
        //BufferedReader bf = new BufferedReader(new FileReader("/com/textris/core/Twordlist_curated.txt"));
        
        InputStream input = Game.class.getResourceAsStream("/Twordlist_curated.txt");
        BufferedReader bf = new BufferedReader(new java.io.InputStreamReader(input));
        String line = bf.readLine();
        
        // checking for end of file
        while (line != null) {
            wordList.add(line);
            line = bf.readLine();
        }
      
        bf.close();
        input.close();
        
        String[] words = (String[]) wordList.toArray(new String[0]);
        
        return words;
    }
}
