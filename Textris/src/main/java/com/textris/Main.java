package com.textris;

import javafx.application.Application;
import com.textris.ui.MainMenuUI;

/**
 * This class starts the program.
 *
 * Responsibilities:
 * - Runs the program
 *
 * Collaborators:
 * - Pretty much everything
 */
public class Main {
    public static void main(String[] args) throws InterruptedException{
        System.out.println("Launching textris game");

        Application.launch(MainMenuUI.class); // Launch JavaFX UI

        System.out.println("Exiting textris game.");
    }
}
