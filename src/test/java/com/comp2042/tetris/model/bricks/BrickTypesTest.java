package com.comp2042.tetris.model.bricks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class BrickTypesTest {

    // ===================== Common Brick Behavior Tests =====================

    @Nested
    @DisplayName("Common Brick Behavior")
    class CommonBrickBehavior {

        @Test
        @DisplayName("I-Brick has at least one rotation state")
        void iBrick_hasAtLeastOneRotation() {
            Brick brick = BrickType.I;
            List<int[][]> shapes = brick.getShapeMatrix();
            assertFalse(shapes.isEmpty(), "Brick should have at least one shape");
        }

        @Test
        @DisplayName("All bricks have at least one rotation state")
        void allBricks_haveAtLeastOneRotation() {
            for (BrickType type : BrickType.values()) {
                List<int[][]> shapes = type.getShapeMatrix();
                assertFalse(shapes.isEmpty(), type.name() + " should have at least one shape");
            }
        }

        @Test
        @DisplayName("cloneBrick creates independent copy for all brick types")
        void allBricks_cloneBrick_createsIndependentCopy() {
            for (BrickType type : BrickType.values()) {
                Brick clone = type.cloneBrick();
                assertNotNull(clone, type.name() + " clone should not be null");
                assertNotSame(type, clone, type.name() + " clone should be different object");
            }
        }

        @Test
        @DisplayName("cloneShape returns deep copy for all bricks")
        void allBricks_cloneShape_returnsDeepCopy() {
            for (BrickType type : BrickType.values()) {
                List<int[][]> original = type.getShapeMatrix();
                List<int[][]> clone = type.cloneShape();

                assertNotSame(original, clone, type.name() + " lists should be different objects");

                for (int i = 0; i < original.size(); i++) {
                    assertNotSame(original.get(i), clone.get(i),
                        type.name() + " matrix at index " + i + " should be different object");
                }
            }
        }

        @Test
        @DisplayName("All brick shapes are 4x4")
        void allBricks_shapesAre4x4() {
            for (BrickType type : BrickType.values()) {
                for (int[][] shape : type.getShapeMatrix()) {
                    assertEquals(4, shape.length, type.name() + " shape should have 4 rows");
                    for (int[] row : shape) {
                        assertEquals(4, row.length, type.name() + " each row should have 4 columns");
                    }
                }
            }
        }

        @Test
        @DisplayName("Each brick shape has exactly 4 filled cells")
        void allBricks_haveExactlyFourCells() {
            for (BrickType type : BrickType.values()) {
                for (int[][] shape : type.getShapeMatrix()) {
                    int filledCells = countFilledCells(shape);
                    assertEquals(4, filledCells, type.name() + " tetromino should have exactly 4 cells");
                }
            }
        }
    }

    // ===================== Rotation Count Tests =====================

    @Nested
    @DisplayName("Rotation Count Tests")
    class RotationCountTests {

        @Test
        @DisplayName("I-Brick has 2 rotation states")
        void iBrick_hasTwoRotations() {
            assertEquals(2, BrickType.I.getShapeMatrix().size());
        }

        @Test
        @DisplayName("J-Brick has 4 rotation states")
        void jBrick_hasFourRotations() {
            assertEquals(4, BrickType.J.getShapeMatrix().size());
        }

        @Test
        @DisplayName("L-Brick has 4 rotation states")
        void lBrick_hasFourRotations() {
            assertEquals(4, BrickType.L.getShapeMatrix().size());
        }

        @Test
        @DisplayName("O-Brick has 1 rotation state")
        void oBrick_hasOneRotation() {
            assertEquals(1, BrickType.O.getShapeMatrix().size());
        }

        @Test
        @DisplayName("S-Brick has 2 rotation states")
        void sBrick_hasTwoRotations() {
            assertEquals(2, BrickType.S.getShapeMatrix().size());
        }

        @Test
        @DisplayName("T-Brick has 4 rotation states")
        void tBrick_hasFourRotations() {
            assertEquals(4, BrickType.T.getShapeMatrix().size());
        }

        @Test
        @DisplayName("Z-Brick has 2 rotation states")
        void zBrick_hasTwoRotations() {
            assertEquals(2, BrickType.Z.getShapeMatrix().size());
        }
    }

    // ===================== Individual Brick Type Tests =====================

    @Nested
    @DisplayName("I-Brick Tests")
    class IBrickTests {

        @Test
        @DisplayName("Has horizontal and vertical orientations")
        void iBrick_hasHorizontalAndVerticalShapes() {
            List<int[][]> shapes = BrickType.I.getShapeMatrix();

            assertEquals(2, shapes.size());

            // First shape should be horizontal (4 in a row)
            int[][] horizontal = shapes.get(0);
            assertTrue(isHorizontalLine(horizontal), "First shape should be horizontal line");

            // Second shape should be vertical (4 in a column)
            int[][] vertical = shapes.get(1);
            assertTrue(isVerticalLine(vertical), "Second shape should be vertical line");
        }

        private boolean isHorizontalLine(int[][] shape) {
            for (int r = 0; r < 4; r++) {
                int count = 0;
                for (int c = 0; c < 4; c++) {
                    if (shape[r][c] != 0) count++;
                }
                if (count == 4) return true;
            }
            return false;
        }

        private boolean isVerticalLine(int[][] shape) {
            for (int c = 0; c < 4; c++) {
                int count = 0;
                for (int r = 0; r < 4; r++) {
                    if (shape[r][c] != 0) count++;
                }
                if (count == 4) return true;
            }
            return false;
        }
    }

    @Nested
    @DisplayName("O-Brick Tests")
    class OBrickTests {

        @Test
        @DisplayName("Has only one rotation state (square)")
        void oBrick_hasOnlyOneRotationState() {
            assertEquals(1, BrickType.O.getShapeMatrix().size());
        }

        @Test
        @DisplayName("Forms a 2x2 square")
        void oBrick_forms2x2Square() {
            int[][] shape = BrickType.O.getShapeMatrix().get(0);

            // Find the 2x2 block
            boolean found2x2 = false;
            outer:
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (shape[r][c] != 0 && shape[r][c+1] != 0 &&
                        shape[r+1][c] != 0 && shape[r+1][c+1] != 0) {
                        found2x2 = true;
                        break outer;
                    }
                }
            }
            assertTrue(found2x2, "O-brick should form a 2x2 square");
        }
    }

    @Nested
    @DisplayName("T-Brick Tests")
    class TBrickTests {

        @Test
        @DisplayName("Each rotation is unique")
        void tBrick_eachRotationIsUnique() {
            List<int[][]> shapes = BrickType.T.getShapeMatrix();

            for (int i = 0; i < shapes.size(); i++) {
                for (int j = i + 1; j < shapes.size(); j++) {
                    assertFalse(matricesEqual(shapes.get(i), shapes.get(j)),
                        "Rotation " + i + " and " + j + " should be different");
                }
            }
        }
    }

    @Nested
    @DisplayName("BrickType Enum Tests")
    class BrickTypeEnumTests {

        @Test
        @DisplayName("All 7 brick types exist")
        void allSevenBrickTypesExist() {
            assertEquals(7, BrickType.values().length);
        }

        @Test
        @DisplayName("cloneBrick() returns non-null for all types")
        void cloneBrick_returnsNonNull() {
            for (BrickType type : BrickType.values()) {
                assertNotNull(type.cloneBrick(), type.name() + ".cloneBrick() should not return null");
            }
        }

        @Test
        @DisplayName("prototypes() returns all brick types")
        void prototypes_returnsAllBrickTypes() {
            List<Brick> prototypes = BrickType.prototypes();
            assertEquals(7, prototypes.size());
        }
    }

    // ===================== Helper Methods =====================

    private int countFilledCells(int[][] shape) {
        int count = 0;
        for (int[] row : shape) {
            for (int cell : row) {
                if (cell != 0) count++;
            }
        }
        return count;
    }

    private boolean matricesEqual(int[][] a, int[][] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i].length != b[i].length) return false;
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] != b[i][j]) return false;
            }
        }
        return true;
    }
}
