package com.comp2042.tetris.model.bricks;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomBrickGeneratorTest {

    @Test
    void constructorUsesDefaultPreviewSizeMinimumOfOne() {
        StubBagPolicy bagPolicy = new StubBagPolicy();
        bagPolicy.enqueueBag(new StubBrick(1));

        RandomBrickGenerator generator = new RandomBrickGenerator(bagPolicy, 0);

        Brick preview = generator.getNextBrick();
        assertNotNull(preview);
        assertEquals(1, bagPolicy.getCreateBagCalls());
        assertEquals(1, ((StubBrick) preview).getId());
    }

    @Test
    void getNextBrickDoesNotRemoveFromQueue() {
        StubBagPolicy bagPolicy = new StubBagPolicy();
        bagPolicy.enqueueBag(new StubBrick(5), new StubBrick(6));
        RandomBrickGenerator generator = new RandomBrickGenerator(bagPolicy, 2);

        Brick firstPeek = generator.getNextBrick();
        Brick secondPeek = generator.getNextBrick();
        Brick consumed = generator.getBrick();

        assertSame(firstPeek, secondPeek);
        assertSame(firstPeek, consumed);
        assertEquals(1, bagPolicy.getCreateBagCalls());
    }

    @Test
    void getBrickRefillsWhenQueueFallsBelowPreview() {
        StubBagPolicy bagPolicy = new StubBagPolicy();
        bagPolicy.enqueueBag(new StubBrick(3));
        bagPolicy.enqueueBag(new StubBrick(4));
        RandomBrickGenerator generator = new RandomBrickGenerator(bagPolicy, 1);

        Brick first = generator.getBrick();
        Brick second = generator.getBrick();

        assertEquals(3, ((StubBrick) first).getId());
        assertEquals(4, ((StubBrick) second).getId());
        assertEquals(2, bagPolicy.getCreateBagCalls());
    }

    @Test
    void previewSizeLargerThanSingleBagPullsMultipleBagsOnInit() {
        StubBagPolicy bagPolicy = new StubBagPolicy();
        bagPolicy.enqueueBag(new StubBrick(7));
        bagPolicy.enqueueBag(new StubBrick(8));
        RandomBrickGenerator generator = new RandomBrickGenerator(bagPolicy, 2);

        assertEquals(2, bagPolicy.getCreateBagCalls());
        assertEquals(7, ((StubBrick) generator.getNextBrick()).getId());
    }

    @Test
    void getNextBrickRefillsIfQueueEmptyBeforePeek() {
        StubBagPolicy bagPolicy = new StubBagPolicy();
        bagPolicy.enqueueBag(new StubBrick(9));
        bagPolicy.enqueueBag(new StubBrick(10));
        RandomBrickGenerator generator = new RandomBrickGenerator(bagPolicy, 1);

        assertEquals(9, ((StubBrick) generator.getBrick()).getId());
        Brick peekAfterDepletion = generator.getNextBrick();

        assertEquals(10, ((StubBrick) peekAfterDepletion).getId());
        assertEquals(2, bagPolicy.getCreateBagCalls());
    }

    private static final class StubBagPolicy implements BrickBagPolicy {
        private final Deque<List<Brick>> bags = new ArrayDeque<>();
        private int createBagCalls;

        void enqueueBag(Brick... bricks) {
            bags.addLast(new ArrayList<>(Arrays.asList(bricks)));
        }

        int getCreateBagCalls() {
            return createBagCalls;
        }

        @Override
        public Deque<Brick> createBag(List<Brick> prototypes) {
            createBagCalls++;
            if (bags.isEmpty()) {
                throw new IllegalStateException("No more bags configured for test");
            }
            return new ArrayDeque<>(bags.pollFirst());
        }
    }

    private static final class StubBrick implements Brick {
        private final int id;

        private StubBrick(int id) {
            this.id = id;
        }

        int getId() {
            return id;
        }

        @Override
        public List<int[][]> getShapeMatrix() {
            return List.<int[][]>of(new int[][]{{id}});
        }

        @Override
        public List<int[][]> cloneShape() {
            return List.<int[][]>of(new int[][]{{id}});
        }

        @Override
        public Brick cloneBrick() {
            return new StubBrick(id);
        }
    }
}
