package com.textris.ui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The {@code MainMenuUI} class represents the main menu screen of the Textris game.
 * <p>
 * This class displays the game logo and provides three main buttons for user interaction:
 * <ul>
 *     <li><b>Start Game</b> — Placeholder for starting a new game.</li>
 *     <li><b>Scoreboard</b> — Opens the {@link ScoreboardUI} scene to display top scores.</li>
 *     <li><b>Exit</b> — Closes the application.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Each button has a distinct background color, hover effect, and a subtle drop shadow:
 * <ul>
 *     <li>Start Game: Green</li>
 *     <li>Scoreboard: Blue</li>
 *     <li>Exit: Red</li>
 * </ul>
 * The hover effect slightly lightens the button color and maintains the shadow.
 * </p>
 * 
 * <p>The logo also has a drop shadow to visually pop against the black background.</p>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>{@code
 * public static void main(String[] args) {
 *     MainMenuUI.launchMenu(args);
 * }
 * }</pre>
 * 
 * <h2>UI Effects</h2>
 * <ul>
 *     <li><b>Drop Shadow:</b> Adds depth to buttons and logo, making them stand out against the background.</li>
 *     <li><b>Hover Color:</b> The button color is lightened by 20% when hovered using {@code derive(color, 20%)}.</li>
 * </ul>
 * 
 */
public class MainMenuUI extends Application {

    /** The main JavaFX stage (window) for the application. */
    private static Stage primaryStage;

    /**
     * Starts the JavaFX application by initializing and displaying the main menu.
     * <p>
     * This method sets up the logo, buttons, layout, colors, hover effects, drop shadows, and scene.
     * </p>
     *
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        // Load logo image from resources
        Image logo = new Image(getClass().getResourceAsStream("/images/textris_logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(250);
        logoView.setPreserveRatio(true);

        // Add subtle drop shadow to logo
        DropShadow logoShadow = new DropShadow();
        logoShadow.setColor(Color.BLACK);
        logoShadow.setRadius(10);
        logoShadow.setOffsetX(2);
        logoShadow.setOffsetY(2);
        logoView.setEffect(logoShadow);

        // Create menu buttons
        Button startButton = new Button("Start Game");
        Button scoreboardButton = new Button("Scoreboard");
        Button exitButton = new Button("Exit");

        // Apply colors, hover effects, and drop shadows to buttons
        setButtonStyle(startButton, "#4CAF50"); // Green
        setButtonStyle(scoreboardButton, "#2196F3"); // Blue
        setButtonStyle(exitButton, "#F44336"); // Red

        // Set button event handlers
        startButton.setOnAction(e -> GameWindow.show(primaryStage));
        scoreboardButton.setOnAction(e -> ScoreboardUI.show(primaryStage));
        exitButton.setOnAction(e -> stage.close());

        // Create layout and add nodes
        VBox layout = new VBox(15, logoView, startButton, scoreboardButton, exitButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black;"); // Black background

        // Create scene and display
        Scene scene = new Scene(layout, 400, 400);
        stage.setTitle("Textris - Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Applies background color, font styling, hover effects, and a drop shadow to a button.
     * <p>
     * Hovering over the button slightly lightens the background color by 20% using
     * {@code derive(color, 20%)}. The drop shadow gives depth to the button, making it
     * visually pop against the background.
     * </p>
     *
     * @param button the button to style
     * @param color  the base color in hexadecimal format (e.g., "#4CAF50")
     */
    private void setButtonStyle(Button button, String color) {
        // Drop shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(5);
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        button.setEffect(shadow);

        // Base button style
        button.setStyle(
                "-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14pt;"
        );

        // Hover effect: lighten color while keeping shadow
        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: derive(" + color + ", 20%); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14pt;"
                )
        );
        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14pt;"
                )
        );
    }

    /**
     * Displays the main menu scene on the specified stage.
     * <p>
     * This method can be used by other scenes (e.g., {@link ScoreboardUI})
     * to return the user to the main menu.
     * </p>
     *
     * @param stage the stage on which to display the main menu
     */
    public static void show(Stage stage) {
        try {
            MainMenuUI menu = new MainMenuUI();
            menu.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches the main menu as a standalone JavaFX application.
     * <p>
     * This method is a wrapper around {@link Application#launch(String...)}.
     * </p>
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
