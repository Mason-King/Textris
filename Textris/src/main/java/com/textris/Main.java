package com.textris;

import com.textris.ui.GameWindow;
//import com.textris.model.Dictionary;
import com.textris.model.GameLoop;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Launching textris game");

        // Swing must run oin the EDT, else can lead to future issues.
        /*SwingUtilities.invokeLater(() -> {
            new GameWindow().setVisible(true);
        });*/
        
        /*Dictionary dictionary = new Dictionary();
        
        System.out.println("Searching for word \"beans\"...");
        
        if(dictionary.isValid("beans")){
            System.out.println("Found");
        }
        else {
            System.out.println("Not found");
        }*/
        
        GameLoop loop = new GameLoop();
        
        System.out.println("Exiting textris game.");
    }
    
    /*
    // JASON WATTS - !! TEMP FOR TESTING !!
    public GameCell testWordSearch() {
        char[] word = {'b','e','a','n'};
        char[] word2 = {'r','t'};
        
        int row = 5; int col = 0; int sAnchor = 2;
        
        System.out.println("TEST: Constructing horizontal word \"bean\" at [" + row + ", " + col + "],");
        System.out.println("vertical word \"art\" at [" + row + "," + (col+2) + "],");
        System.out.println("searching at anchor point [" + row + ", " + sAnchor + "]");
        
        LetterBlock newBlock = null;
        
        for(char c : word){
           newBlock = new LetterBlock(c);
           grid[row][col].setBlock(newBlock);
           
           if(col == 2){
               for(char c2 : word2){
                   newBlock = new LetterBlock(c2);
                   grid[++row][col].setBlock(newBlock);
               }
               row = 5;
           }
           
           col++;
        }
        GameCell testBlock = grid[row][sAnchor];
        
        return testBlock;
    }*/
}