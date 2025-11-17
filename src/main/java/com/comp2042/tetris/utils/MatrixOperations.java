/**
 * this class works on doing all calculations related to te game
 * like merging, collision checks, making sure blocks are in bound and cleaning rows
 */

package com.comp2042.tetris.utils;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.score.ScoreCalculator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;


@Pure
public final class MatrixOperations {


    //We don't want to instantiate this utility class
    private MatrixOperations(){

    }

    @Pure
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int row = 0; row < brick.length; row++) {
            for (int col = 0; col < brick[row].length; col++) {
                int targetRow = y + row;
                int targetCol = x + col;
                if (brick[row][col] != 0 && (isOutOfBounds(matrix, targetRow, targetCol) || matrix[targetRow][targetCol] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    //the point of the if statement added is to fix the logic of OutOfBounds
    //now if player attempts to rotate on the edges it gets blocked
    @Pure
    private static boolean isOutOfBounds(int[][] matrix, int targetRow, int targetCol) {
        if (targetRow < 0 || targetRow >= matrix.length) {
            return true;
        }
        return targetCol < 0 || targetCol >= matrix[targetRow].length;
    }


    @Pure
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

   @Pure
   public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
       for (int row = 0; row < brick.length; row++) {
           for (int col = 0; col < brick[row].length; col++) {
               int targetRow = y + row;
               int targetCol = x + col;
               if (brick[row][col] != 0) {
                   copy[targetRow][targetCol] = brick[row][col];
                }
            }
        }
        return copy;
    }

    @Pure
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
            int[] tmpRow = new int[matrix[rowIndex].length];
            boolean rowToClear = true;
            for (int colIndex = 0; colIndex < matrix[rowIndex].length; colIndex++) {
                if (matrix[rowIndex][colIndex] == 0) {
                    rowToClear = false;
                }
                tmpRow[colIndex] = matrix[rowIndex][colIndex];
            }
            if (rowToClear) {
                clearedRows.add(rowIndex);
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int rowIndex = matrix.length - 1; rowIndex >= 0; rowIndex--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[rowIndex] = row;
            } else {
                break;
            }
        }
        int linesRemoved = clearedRows.size();
        int scoreBonus = ScoreCalculator.calculateRowClearBonus(linesRemoved);
        return new ClearRow(linesRemoved, tmp, scoreBonus);
    }

    @Pure
    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
