package com.textris;

import com.textris.ui.GameWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Launching textris game");

        // Swing must run oin the EDT, else can lead to future issues.
        SwingUtilities.invokeLater(() -> {
            new GameWindow().setVisible(true);
        });

        System.out.println("Exiting textris game.");
    }
}