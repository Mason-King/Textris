package com.textris.ui;

import com.textris.model.LetterBlock;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.textris.media.Block;

public class GameWindow {
    public static final int SIZE = 60;
    public static int XMAX = SIZE * 5;
    public static int YMAX = SIZE * 8;
    public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE];

    private static Pane pane = new Pane();
    private static Scene scene;
    private static Block nextBlock = new Block('x');
    private static StackPane overlay = new StackPane();

    public static void show(Stage primaryStage) {
        Line line = new Line(XMAX, 0, XMAX, YMAX);
        line.setStroke(Color.WHITE);
        pane.getChildren().add(line);

        overlay.setPickOnBounds(false);
        overlay.setVisible(false);

        StackPane root = new StackPane(pane, overlay);
        scene = new Scene(root, XMAX + 180, YMAX);
        pane.setStyle("-fx-background-color: black;");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Textris - Game Window");
        primaryStage.show();
    }

    public static Scene getScene() {
        return scene;
    }

    public static Block getActiveBlock() {
        return nextBlock;
    }

    public static void addBlock(LetterBlock letterBlock) {
        if (letterBlock == null || letterBlock.getBlock() == null) return;

        var blockNode = letterBlock.getBlock().getBlock();

        blockNode.setLayoutX(letterBlock.getCol() * SIZE);
        blockNode.setLayoutY(letterBlock.getRow() * SIZE);

        Platform.runLater(() -> pane.getChildren().add(blockNode));
    }

    public static void removeBlockNode(StackPane node) {
        Platform.runLater(() -> {
            if (node != null && pane.getChildren().contains(node)) {
                pane.getChildren().remove(node);
            }
        });
    }

    public static void refreshBoard() {
        Platform.runLater(() -> pane.requestLayout());
    }

    /** Clears all game blocks visually (used when restarting). */
    public static void clearBoardUI() {
        Platform.runLater(() -> {
            pane.getChildren().removeIf(node -> node instanceof StackPane);
            refreshBoard();
        });
    }

    /** Displays a "Game Over" overlay with a Restart button. */
    public static void showGameOverOverlay(Runnable onRestart) {
        Platform.runLater(() -> {
            overlay.getChildren().clear();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
            overlay.setVisible(true);

            Text gameOverText = new Text("GAME OVER");
            gameOverText.setFont(Font.font("Arial", 48));
            gameOverText.setFill(Color.RED);

            Button restartButton = new Button("Restart");
            restartButton.setFont(Font.font("Arial", 24));
            restartButton.setStyle("""
                -fx-background-color: #2ecc71;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-pref-width: 200;
                -fx-pref-height: 60;
                -fx-background-radius: 12;
            """);

            restartButton.setOnMouseEntered(e -> restartButton.setStyle("""
                -fx-background-color: #27ae60;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-pref-width: 200;
                -fx-pref-height: 60;
                -fx-background-radius: 12;
            """));

            restartButton.setOnMouseExited(e -> restartButton.setStyle("""
                -fx-background-color: #2ecc71;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-pref-width: 200;
                -fx-pref-height: 60;
                -fx-background-radius: 12;
            """));

            restartButton.setOnAction(e -> {
                overlay.setVisible(false);
                if (onRestart != null) {
                    onRestart.run();
                }
            });

            VBox layout = new VBox(20, gameOverText, restartButton);
            layout.setStyle("-fx-alignment: center;");
            overlay.getChildren().add(layout);
        });
    }
}


