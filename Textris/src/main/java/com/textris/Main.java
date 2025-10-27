package com.textris;

import com.textris.model.Dictionary;
import com.textris.model.GameBoard;
import com.textris.model.GameLoop;
import com.textris.ui.GameWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        System.out.println("Launching textris game");

        GameBoard gameBoard = new GameBoard();
        Dictionary dictionary = new Dictionary();
        GameLoop gameLoop = new GameLoop(gameBoard, dictionary);

        while (!gameLoop.isGameOver()) {
            gameLoop.tick();
            Thread.sleep(500);
        }

        System.out.println("Exiting textris game.");
    }
}