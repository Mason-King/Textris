package com.textris.ui;

import com.textris.media.Block;
import com.textris.model.Direction;
import com.textris.model.GameBoard;
import com.textris.model.GameCell;
import com.textris.model.LetterBlock;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

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
public class InputHandler 
{
    private final GameBoard board;
    private GameCell currentCell;
    private LetterBlock currentBlock;
    private Block currentBlockSprite;

    /**
     * Creates a new InputHandler that listens for keyboard input.
     *
     * @param scene the JavaFX scene to attach key listeners to
     * @param gameBoard the GameBoard used to control movement and collision checks
     */
    public InputHandler(Scene scene, GameBoard gameBoard)
    {
        this.board = gameBoard;
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
     * Returns the current cell where the active block resides.
     *
     * @return the active cell
     */
    public GameCell getActiveCell() 
    {
        return this.currentCell;
    }

    /**
     * Sets the currently active block that responds to player input.
     *
     * @param block the currently falling block
     * @param sprite the UI representation of the currently falling block
     */
    public void setActiveBlock(LetterBlock block, Block sprite) 
    {
        this.currentBlock = block;
        this.currentBlockSprite = sprite;
    }

    /**
     * Initializes key listeners on the given scene.
     *   Left Arrow — move block left
     *   Right Arrow — move block right
     *   Down Arrow — move block down
     *
     * @param scene the JavaFX scene to attach listeners to
     */
    private void initializeKeyListeners(Scene scene) {
        scene.setOnKeyPressed(event ->
        {
            if (currentBlock == null) {
                return;
            }

            KeyCode key = event.getCode();

            switch (key) {
                case LEFT:
                    handleMove(Direction.LEFT);
                    break;
                case RIGHT:
                    handleMove(Direction.RIGHT);
                    break;
                case DOWN:
                    handleMove(Direction.DOWN);
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
     private void handleMove(Direction direction) {
         //If the block can move in intended direction, If its against other blocks and cant move down no more moving!
         if (board.canMove(currentBlock, direction) && board.canMove(currentBlock, Direction.DOWN)) {
             board.move(currentBlock, direction);
             updateActiveCell(currentBlock);
         }
     }

     /**
     * Updates the current active cell with a new block.
     * 
     * @param block the new active block
     */
    public void updateActiveCell(LetterBlock block) {
        if (block != null) {
            this.currentCell = board.getCell(block.getCol(), block.getRow());
        } else {
            this.currentCell = null;
        }
    }
}
