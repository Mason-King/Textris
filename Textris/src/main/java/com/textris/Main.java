package com.textris;

import javafx.application.Application;
import com.textris.ui.MainMenuUI;

public class Main {
    public static void main(String[] args) {
        System.out.println("Launching textris game");

        Application.launch(MainMenuUI.class); // Launch JavaFX UI

        System.out.println("Exiting textris game.");
    }
}   
