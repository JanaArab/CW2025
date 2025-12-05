package com.comp2042.tetris.controller.command;

/**
 * Represents a game command that can be executed.
 * Implements the Command design pattern for handling user inputs.
 *
 * <p>Commands encapsulate game actions such as:</p>
 * <ul>
 *   <li>Moving bricks (left, right, down)</li>
 *   <li>Rotating bricks</li>
 *   <li>Instant drop (hard drop)</li>
 * </ul>
 *
 * @see MoveLeftCommand
 * @see MoveRightCommand
 * @see MoveDownCommand
 * @see RotateCommand
 * @see InstantDropCommand
 */
public interface GameCommand {

    /**
     * Executes this command.
     */
    void execute();
}
