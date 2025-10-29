/**
 * This class takes user input the move a LetterBlock on the GameBoard.
 *
 * Responsibilities:
 * - Read user input using arrow keys
 * - Call GameBoard methods based on the key pressed
 *
 * Collaborators:
 * - GameBoard
 * - LetterBlock
 */

package com.textris.ui;

import com.textris.model.GameBoard;
import com.textris.model.GameCell;
import com.textris.model.LetterBlock;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputHandler 
{
    // private final GameBoard board;
    private GameCell currentCell;
    private LetterBlock currentBlock;

    /**
     * Creates a new InputHandler that listens for keyboard input.
     *
     * @param scene the JavaFX scene to attach key listeners to
     * @param gameBoard the GameBoard used to control movement and collision checks
     */
    public InputHandler(Scene scene, GameBoard gameBoard)
    {
        //this.board = gameBoard;
        initializeKeyListeners(scene);
    }

    /**
     * Sets the GameCell that the currently active block starts in.
     *
     * @param cell the starting cell
     */
    public void setActiveCell(GameCell cell) 
    {
        this.currentCell = cell;
    }

    /**
     * Sets the currently active block that responds to player input.
     *
     * @param block the currently falling block
     */
    public void setActiveBlock(LetterBlock block) 
    {
        this.currentBlock = block;
    }

    /**
     * Initializes key listeners on the given scene.
     *   Left Arrow — move block left
     *   Right Arrow — move block right
     *   Down Arrow — move block down
     *
     * @param scene the JavaFX scene to attach listeners to
     */
    private void initializeKeyListeners(Scene scene) 
    {
        scene.setOnKeyPressed(event -> 
        {
            if (currentBlock == null) {
                return;
            }

            KeyCode key = event.getCode();

            switch (key) {
                case LEFT:
                    moveCurrentLeft();
                    break;
                case RIGHT:
                    moveCurrentRight();
                    break;
                case DOWN:
                    moveCurrentDown();
                    break;
                default:
                    // Ignore other keys
                    break;
            }
        });
    }

    
    /**
     * Uses GameBoard and Scene to move the current active LetterBlock left.
     */
    private void moveCurrentLeft()
    {
        currentCell.moveLeft();
    }
    

    /**
     * Uses GameBoard and Scene to move the current active LetterBlock right.
     */
    private void moveCurrentRight()
    {
        currentCell.moveRight();
    }


    /**
     * Uses GameBoard and Scene to move the current active LetterBlock down.
     */
    private void moveCurrentDown()
    {
        currentCell.moveDown();
    }

// Possibly use JAVAFX to move the physical rectangle (block) created in media.Block and defined (size) in ui.GameWindow !!!!!!




    // /**
    //  * Attempts to move the active block in the specified direction.
    //  *
    //  * <p>Valid directions are {@code "LEFT"}, {@code "RIGHT"}, and {@code "DOWN"}.</p>
    //  *
    //  * @param direction the direction to move the block
    //  */
    // private void handleMove(String direction) {
    //     if (gameBoard.canMove(activeBlock, direction)) {
    //         gameBoard.move(activeBlock, direction);
    //     }
    // }
}
