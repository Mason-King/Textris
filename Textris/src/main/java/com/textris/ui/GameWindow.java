package com.textris.ui;

import com.textris.model.GameLoop;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameWindow {
    private JPanel rootPanel;
    private JLabel mainLabel;
    private JButton startGameBtn;

    //Initialize the game loop somewhere here?
//    public void initLoop() {
//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        executor.scheduleAtFixedRate(() -> {
//            //Need instance of game loop here
//        }, 0, 500, TimeUnit.MILLISECONDS);
//    }
}
