package com.textris.media;

import com.textris.ui.GameWindow;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Block 
{
    private Rectangle shape;
    private Color color = Color.BEIGE;
    private Color strColor = Color.BURLYWOOD;
    private int size = GameWindow.SIZE;
    StackPane stackPane = new StackPane();

    /**
     * Creates a physical block based on an existing LetterBlock's stored char.
     * 
     * @param name the character displayed on the Block
     */
    public Block(char name)
    {
        this.shape = new Rectangle(size, size);
        this.shape.setFill(color);
        shape.setStroke(strColor);
        shape.setStrokeWidth(5);
        shape.setArcWidth(10); 
        shape.setArcHeight(10);

        Text text = new Text(String.valueOf(name));
        text.setFont(Font.font("Comic Sans", 20));
        text.setFill(Color.BLACK);

        stackPane.getChildren().addAll(shape, text);
        StackPane.setAlignment(text, Pos.CENTER);
    }

    /**
     * Returns the StackPane that stores the Block.
     * 
     * @return StackPane
     */
    public StackPane getBlock()
    {
        return stackPane;
    }
}