package com.textris.ui;

import com.textris.storage.ScoreEntry;
import com.textris.storage.ScoreManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;

/**
 * The ScoreboardUI class defines the JavaFX user interface for displaying
 * the top five highest scores in the game.
 * 
 * This screen presents a table showing each player's name and score,
 * sorted in descending order (highest to lowest).
 *
 * Features:
 *     Displays the top 5 highest scores using a TableView
 *     Allows users to return to the main menu using a "Back" button
 *     Automatically updates score order when new entries are added
 */
public class ScoreboardUI {

    /**
     * Displays the scoreboard scene on the provided Stage.
     * 
     * The scoreboard includes a table displaying
     * player names and scores in descending order.
     * 
     *
     * @param primaryStage the primary stage of the JavaFX application
     */
    public static void show(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // -------------------------
        // Top Section: Title & Back
        // -------------------------
        HBox topBar = new HBox();
        topBar.setSpacing(10);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("← Back");
        setButtonStyle(backButton, "#2196F3"); // Blue with hover effect
        backButton.setOnAction(e -> MainMenuUI.show(primaryStage));

        Label titleLabel = new Label("Highest Scores");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.RED);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        // Subtle glow effect on title
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setRadius(15);
        glow.setSpread(0.3);
        titleLabel.setEffect(glow);

        topBar.getChildren().addAll(backButton, titleLabel);
        root.setTop(topBar);

        // -------------------------
        // Table Section: Scoreboard
        // -------------------------
        TableView<ScoreEntry> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN); // Columns fill table

        // Drop shadow for table
        DropShadow tableShadow = new DropShadow();
        tableShadow.setRadius(10);
        tableShadow.setOffsetX(3);
        tableShadow.setOffsetY(3);
        tableShadow.setColor(Color.rgb(0, 0, 0, 0.5)); // semi-transparent black
        table.setEffect(tableShadow);

        // Placeholder styling
        Label placeholder = new Label("No scores to display.");
        placeholder.setTextFill(Color.LIGHTGRAY);
        placeholder.setFont(Font.font("Arial", FontPosture.ITALIC, 16));
        table.setPlaceholder(placeholder);

        // Columns
        TableColumn<ScoreEntry, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        nameCol.setStyle("-fx-alignment: CENTER;");
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.5));

        TableColumn<ScoreEntry, Number> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getScore()));
        scoreCol.setStyle("-fx-alignment: CENTER;");
        scoreCol.prefWidthProperty().bind(table.widthProperty().multiply(0.5));

        table.getColumns().addAll(nameCol, scoreCol);

        // Load and display saved scores
        List<ScoreEntry> scores = ScoreManager.loadScores();
        table.getItems().addAll(scores);

        // -------------------------
        // Alternate Row Colors
        // -------------------------
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ScoreEntry item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else if (getIndex() % 2 == 0) {
                    setStyle("-fx-background-color: #1a1a1a;"); // Dark gray
                } else {
                    setStyle("-fx-background-color: black;"); // Black
                }
            }
        });

        root.setCenter(table);

        // -------------------------
        // Scene Setup
        // -------------------------
        Scene scene = new Scene(root, 400, 300);
        root.setStyle("-fx-background-color: black;"); // Black background
        primaryStage.setScene(scene);
        primaryStage.setTitle("Scoreboard");
        primaryStage.show();
    }

    /**
     * Applies background color, hover effect, and drop shadow to a button.
     * 
     * Hovering slightly lightens the color to provide feedback to the user.
     * 
     *
     * @param button the button to style
     * @param color  the base color in hexadecimal format (e.g., "#2196F3")
     */
    private static void setButtonStyle(Button button, String color) {
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

        // Hover effect: lighten color
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
}
