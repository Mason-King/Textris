package com.textris.ui;

import com.textris.model.LetterBlock;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

import com.textris.media.Block;

public class GameWindow
{
    public static final int SIZE = 60;
    public static int XMAX = SIZE * 5;
    public static int YMAX = SIZE * 8;
    public static int[][] MESH = new int[XMAX/SIZE][YMAX/SIZE];
    private static Pane pane = new Pane();

    private static Scene scene;

    private static Block nextBlock = new Block('x');

    public static void show(Stage primaryStage)
    {
        Line line = new Line(XMAX, 0, XMAX, YMAX);
        line.setStroke(Color.WHITE);
        pane.getChildren().add(line);


        scene = new Scene(pane, XMAX + 180, YMAX);
        pane.setStyle("-fx-background-color: black;");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Textris - Game Window");
        primaryStage.show();
    }

    public static Scene getScene()
    {
        return scene;
    }

    public static Block getActiveBlock()
    {
        return nextBlock;
    }

    public static void addBlock(LetterBlock letterBlock) {
        if (letterBlock == null || letterBlock.getBlock() == null) return;

        var blockNode = letterBlock.getBlock().getBlock();

        // Set position (based on grid coordinates)
        blockNode.setLayoutX(letterBlock.getCol() * SIZE);
        blockNode.setLayoutY(letterBlock.getRow() * SIZE);

        // Add to the UI
        Platform.runLater(() -> {
            pane.getChildren().add(blockNode);
        });
    }
    
    /**
    * Removes a block node from the game board UI safely.
    * @param node the StackPane node to remove
    */
    public static void removeBlockNode(javafx.scene.layout.StackPane node) {
        Platform.runLater(() -> {
            if (node != null && pane.getChildren().contains(node)) {
                pane.getChildren().remove(node);
            }
        });
    }

    /**
    * Forces a visual refresh of the game board after updates.
    */
    public static void refreshBoard() {
    Platform.runLater(() -> pane.requestLayout());
    }
}
