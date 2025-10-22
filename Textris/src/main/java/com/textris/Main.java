package com.textris;

import com.textris.ui.GameWindow;
import com.textris.model.Dictionary;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Launching textris game");

        // Swing must run oin the EDT, else can lead to future issues.
        /*SwingUtilities.invokeLater(() -> {
            new GameWindow().setVisible(true);
        });*/
        
        Dictionary dictionary = new Dictionary();
        
        System.out.println("Searching for word \"beans\"...");
        
        if(dictionary.isValid("beans")){
            System.out.println("Found");
        }
        else {
            System.out.println("Not found");
        }
        
        System.out.println("Exiting textris game.");
    }
}