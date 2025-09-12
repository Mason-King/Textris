package com.textris;

import com.textris.core.Game;

public class Main {
    public static void main(String[] args) {
        System.out.println("Launching textris game...");

        Game game = new Game();
        game.start();

        System.out.println("Exiting textris game...");
    }
}