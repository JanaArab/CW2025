package com.comp2042.tetris.model.event;

/**
 * Event record representing that a brick has been placed on the board.
 * This event is published when a falling brick lands and is locked in place.
 *
 * <p>Listeners can use this event to trigger effects like:</p>
 * <ul>
 *   <li>Sound effects for brick landing</li>
 *   <li>Visual feedback animations</li>
 *   <li>Score updates for placement</li>
 * </ul>
 *
 * @see GameEventPublisher#publishBrickPlaced(BrickPlacedEvent)
 * @see GameEventListener#onBrickPlaced(BrickPlacedEvent)
 */
public record BrickPlacedEvent() {
}
