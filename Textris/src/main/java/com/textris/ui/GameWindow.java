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

/**
 * Handles the main in-game UI window.
 * 
 * Responsibilities:
 * -Displays the grid 
 * -displays falling blocks, overlays, and score.
 * 
 */
public class GameWindow {

    /** The size of each cell/block in pixels. */
    public static final int SIZE = 60;

    /** The total width of the game area in pixels. */
    public static int XMAX = SIZE * 5;

    /** The total height of the game area in pixels. */
    public static int YMAX = SIZE * 8;

    /** The logical grid representation of the game space. */
    public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE];

    /** The main Pane that displays all visible game elements. */
    private static Pane pane = new Pane();

    /** The current Scene displayed in the game window. */
    private static Scene scene;

    /** The currently active block (next falling block). */
    private static Block nextBlock = new Block('x');

    /** StackPane used to display overlays such as Game Over. */
    private static StackPane overlay = new StackPane();

    /** Text node displaying the player’s score. */
    private static Text scoreText = new Text("Score: 0");

    /**
     * Initializes and displays the main game window.
     *
     * @param primaryStage the primary JavaFX stage to display the scene on
     */
public static void show(Stage primaryStage) {
    pane.getChildren().clear();
    scoreText.setText("Score: 0"); // Reset score display each time a new game starts

    Line line = new Line(XMAX+4, 0, XMAX+4, YMAX);
    line.setStroke(Color.WHITE);
    pane.getChildren().add(line);

    // Score display setup
    scoreText.setFont(Font.font("Arial", 24));
    scoreText.setFill(Color.WHITE);
    scoreText.setLayoutX(XMAX + 40);
    scoreText.setLayoutY(60);
    pane.getChildren().add(scoreText);

    overlay.setPickOnBounds(false);
    overlay.setVisible(false);

    StackPane root = new StackPane(pane, overlay);
    scene = new Scene(root, XMAX + 180, YMAX);
    pane.setStyle("-fx-background-color: black;");
    primaryStage.setScene(scene);
    primaryStage.setTitle("Textris - Game Window");
    primaryStage.show();
}


    /**
     * Returns the current JavaFX Scene associated with the GameWindow.
     *
     * @return the active game Scene
     */
    public static Scene getScene() {
        return scene;
    }

    /**
     * Returns the currently active Block.
     *
     * @return the active Block
     */
    public static Block getActiveBlock() {
        return nextBlock;
    }

    /**
     * Adds a new LetterBlock’s node to the visual game board.
     *
     * @param letterBlock the block to add to the UI
     */
    public static void addBlock(LetterBlock letterBlock) {
        if (letterBlock == null || letterBlock.getBlock() == null) return;

        var blockNode = letterBlock.getBlock().getBlock();

        blockNode.setLayoutX(letterBlock.getCol() * SIZE);
        blockNode.setLayoutY(letterBlock.getRow() * SIZE);

        Platform.runLater(() -> pane.getChildren().add(blockNode));
    }

    /**
     * Removes a visual block node from the board if present.
     *
     * @param node the StackPane node representing the block
     */
    public static void removeBlockNode(StackPane node) {
        Platform.runLater(() -> {
            if (node != null && pane.getChildren().contains(node)) {
                pane.getChildren().remove(node);
            }
        });
    }

    /**
     * Forces a UI refresh for the game pane layout.
     */
    public static void refreshBoard() {
        Platform.runLater(() -> pane.requestLayout());
    }

    /** 
     * Clears all game blocks visually (used when restarting or resetting the board).
     */
    public static void clearBoardUI() {
        Platform.runLater(() -> {
            pane.getChildren().removeIf(node -> node instanceof StackPane);
            refreshBoard();
        });
    }

    /**
     * Updates the on-screen score display.
     *
     * @param newScore the player's updated score
     */
    public static void updateScore(int newScore) {
        Platform.runLater(() -> scoreText.setText("Score: " + newScore));
    }

    /**
     * Displays a Game Over overlay containing buttons for restarting the game
     * or returning to the main menu.
     *
     * @param onRestart a callback Runnable executed when the player selects Restart
     */
    public static void showGameOverOverlay(Runnable onRestart) {
        Platform.runLater(() -> {
            overlay.getChildren().clear();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
            overlay.setVisible(true);

            Text gameOverText = new Text("GAME OVER");
            gameOverText.setFont(Font.font("Arial", 48));
            gameOverText.setFill(Color.RED);

            // --- Restart Button Setup ---
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

            // --- Main Menu Button Setup ---
            Button mainMenuButton = new Button("Main Menu");
            mainMenuButton.setFont(Font.font("Arial", 24));
            mainMenuButton.setStyle("""
                -fx-background-color: #3498db;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-pref-width: 260;
                -fx-pref-height: 60;
                -fx-background-radius: 12;
            """);

            mainMenuButton.setOnMouseEntered(e -> mainMenuButton.setStyle("""
                -fx-background-color: #2980b9;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-pref-width: 260;
                -fx-pref-height: 60;
                -fx-background-radius: 12;
            """));

            mainMenuButton.setOnMouseExited(e -> mainMenuButton.setStyle("""
                -fx-background-color: #3498db;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-pref-width: 260;
                -fx-pref-height: 60;
                -fx-background-radius: 12;
            """));

            mainMenuButton.setOnAction(e -> {
                Stage stage = (Stage) scene.getWindow();
                try {
                    new com.textris.ui.MainMenuUI().start(stage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            // --- Layout ---
            VBox layout = new VBox(20, gameOverText, restartButton, mainMenuButton);
            layout.setStyle("-fx-alignment: center;");
            overlay.getChildren().add(layout);
        });
    }
}




