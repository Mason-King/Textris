package com.textris.ui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

public class GameWindow
{
    public static final int SIZE = 60;
    public static int XMAX = SIZE * 5;
    public static int YMAX = SIZE * 8;
    public static int[][] MESH = new int[XMAX/SIZE][YMAX/SIZE];
    private static Pane pane = new Pane();

    //private static Block nextBlock = Block.create();

    public static void show(Stage primaryStage)
    {
        Line line = new Line(XMAX, 0, XMAX, YMAX);
        line.setStroke(Color.BLUEVIOLET);
        pane.getChildren().add(line);




        Scene scene = new Scene(pane, XMAX + 180, YMAX);
        pane.setStyle("-fx-background-color: black;");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Textris - Game Window");
        primaryStage.show();
    }



}
