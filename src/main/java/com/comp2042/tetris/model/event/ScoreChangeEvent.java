package com.comp2042.tetris.model.event;

/**
 * Event record representing a change in the player's score.
 * Published whenever the score is updated (drops, line clears, etc.).
 *
 * @param newScore the new total score value
 * @see GameEventPublisher#publishScoreChanged(ScoreChangeEvent)
 * @see GameEventListener#onScoreChanged(ScoreChangeEvent)
 */
public record ScoreChangeEvent(int newScore) {
}
