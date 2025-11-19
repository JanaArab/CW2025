package com.comp2042.tetris.model.score;

import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.EventType;
import com.comp2042.tetris.model.event.MoveEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultScorePolicyTest {

    private final DefaultScorePolicy policy = new DefaultScorePolicy();

    @Test
    void awardsPointWhenEventSourceIsUser() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);

        assertEquals(1, policy.calculateDropScore(event));
    }

    @Test
    void awardsZeroWhenEventSourceIsThread() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.THREAD);

        assertEquals(0, policy.calculateDropScore(event));
    }
}
