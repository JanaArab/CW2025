package com.comp2042.tetris.model.bricks;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShuffleBagPolicyTest {

    private final ShuffleBagPolicy policy = new ShuffleBagPolicy();

    @Test
    void createBagClonesEachPrototypeAndReturnsArrayDeque() {
        List<TrackingBrick> prototypes = List.of(
                new TrackingBrick(1),
                new TrackingBrick(2),
                new TrackingBrick(3)
        );
        List<Brick> input = toBrickList(prototypes);

        Deque<Brick> bag = policy.createBag(input);

        assertEquals(ArrayDeque.class, bag.getClass());
        assertEquals(prototypes.size(), bag.size());
        List<Brick> clones = new ArrayList<>(bag);
        for (int i = 0; i < prototypes.size(); i++) {
            TrackingBrick original = prototypes.get(i);
            assertEquals(1, original.getCloneCalls(), "prototype should be cloned exactly once");
        }
        for (Brick clone : clones) {
            assertTrue(clone instanceof TrackingBrick);
            TrackingBrick trackingClone = (TrackingBrick) clone;
            assertTrue(containsId(prototypes, trackingClone.getId()));
            assertFalse(prototypes.contains(clone), "clone must be a distinct instance from prototypes");
        }
    }

    @Test
    void createBagDoesNotMutatePrototypeList() {
        List<TrackingBrick> prototypes = new ArrayList<>(List.of(new TrackingBrick(10), new TrackingBrick(20)));
        List<TrackingBrick> snapshot = new ArrayList<>(prototypes);
        List<Brick> input = toBrickList(prototypes);

        policy.createBag(input);

        assertEquals(snapshot, prototypes);
    }

    @Test
    void createBagReturnsEmptyDequeForEmptyInput() {
        Deque<Brick> bag = policy.createBag(new ArrayList<>());

        assertTrue(bag.isEmpty());
        assertEquals(ArrayDeque.class, bag.getClass());
    }

    @Test
    void createBagEventuallyProducesDifferentOrder() {
        List<TrackingBrick> prototypes = List.of(
                new TrackingBrick(1),
                new TrackingBrick(2),
                new TrackingBrick(3),
                new TrackingBrick(4)
        );
        List<Integer> originalOrder = List.of(1, 2, 3, 4);

        boolean observedDifferentOrder = false;
        for (int attempt = 0; attempt < 25 && !observedDifferentOrder; attempt++) {
            List<Brick> input = toBrickList(prototypes);
            Deque<Brick> bag = policy.createBag(input);
            List<Integer> ids = bag.stream()
                    .map(brick -> ((TrackingBrick) brick).getId())
                    .toList();
            if (!ids.equals(originalOrder)) {
                observedDifferentOrder = true;
            }
        }

        assertTrue(observedDifferentOrder, "Expected shuffle to yield a different order across multiple attempts");
    }

    private static boolean containsId(List<TrackingBrick> bricks, int id) {
        return bricks.stream().anyMatch(brick -> brick.getId() == id);
    }

    private static List<Brick> toBrickList(List<? extends Brick> bricks) {
        List<Brick> list = new ArrayList<>(bricks.size());
        list.addAll(bricks);
        return list;
    }

    private static final class TrackingBrick implements Brick {
        private final int id;
        private int cloneCalls;

        private TrackingBrick(int id) {
            this.id = id;
        }

        int getId() {
            return id;
        }

        int getCloneCalls() {
            return cloneCalls;
        }

        @Override
        public List<int[][]> getShapeMatrix() {
            return cloneMatrix(new int[][]{{id}});
        }

        @Override
        public List<int[][]> cloneShape() {
            return cloneMatrix(new int[][]{{id}});
        }

        private List<int[][]> cloneMatrix(int[][] matrix) {
            int[][] copy = new int[matrix.length][];
            for (int i = 0; i < matrix.length; i++) {
                copy[i] = Arrays.copyOf(matrix[i], matrix[i].length);
            }
            List<int[][]> list = new ArrayList<>(1);
            list.add(copy);
            return list;
        }

        @Override
        public Brick cloneBrick() {
            cloneCalls++;
            return new TrackingBrick(id);
        }
    }
}
