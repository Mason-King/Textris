package com.textris.ui;

import com.textris.model.GameBoard;
import com.textris.model.LetterBlock;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Handles keyboard input for controlling the falling {@link LetterBlock}
 * in the Tetris-like game.
 *
 * <p>This class listens for arrow key presses and moves the active block
 * left, right, or down if allowed by the {@link GameBoard} rules.</p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * InputHandler inputHandler = new InputHandler(scene, gameBoard);
 * inputHandler.setActiveBlock(currentBlock);
 * }</pre>
 *
 * <p>When a new block spawns, call {@link #setActiveBlock(LetterBlock)}
 * so the player can control it.</p>
 */
public class InputHandler {

    /** Reference to the game's board, which handles block movement. */
    private final GameBoard gameBoard;

    /** The currently active falling block controlled by the player. */
    private LetterBlock activeBlock;

    /**
     * Creates a new {@code InputHandler} that listens for keyboard input
     * on the given JavaFX {@link Scene}.
     *
     * @param scene the JavaFX scene to attach key listeners to
     * @param gameBoard the {@link GameBoard} used to control movement and collision
     */
    public InputHandler(Scene scene, GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        initializeKeyListeners(scene);
    }

    /**
     * Sets the currently active block that responds to player input.
     *
     * <p>This should be updated whenever a new {@link LetterBlock} spawns
     * or when the current one locks into place.</p>
     *
     * @param block the currently falling block
     */
    public void setActiveBlock(LetterBlock block) {
        this.activeBlock = block;
    }

    /**
     * Initializes key listeners on the given scene.
     * <ul>
     *     <li>⬅️ Left Arrow — move block left</li>
     *     <li>➡️ Right Arrow — move block right</li>
     *     <li>⬇️ Down Arrow — move block down</li>
     * </ul>
     *
     * @param scene the JavaFX scene to attach listeners to
     */
    private void initializeKeyListeners(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (activeBlock == null) {
                return; // No active block to control
            }

            KeyCode key = event.getCode();

            switch (key) {
                case LEFT:
                    handleMove("LEFT");
                    break;
                case RIGHT:
                    handleMove("RIGHT");
                    break;
                case DOWN:
                    handleMove("DOWN");
                    break;
                default:
                    // Ignore other keys
                    break;
            }
        });
    }

    /**
     * Attempts to move the active block in the specified direction.
     *
     * <p>Valid directions are {@code "LEFT"}, {@code "RIGHT"}, and {@code "DOWN"}.</p>
     *
     * @param direction the direction to move the block
     */
    private void handleMove(String direction) {
        if (gameBoard.canMove(activeBlock, direction)) {
            gameBoard.move(activeBlock, direction);
        }
    }
}
