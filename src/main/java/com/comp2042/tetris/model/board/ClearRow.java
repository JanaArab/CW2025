/// stores the matrix the includes all data regarding row deletion
/// represents the result of deleting a row ,the updated matrix
/// and score bonus
package com.comp2042.tetris.model.board;


import com.comp2042.tetris.model.data.MatrixOperations;

/**converted into a record
because the class consisted of constructors and getters only
 with no logic or behavior, hence we switched to record as it
 serves the same purpose with fewer lines of code */
public record ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
    @Override
    public int[][] newMatrix() {
        return MatrixOperations.copy(newMatrix);
    }
}

/**public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    public int getLinesRemoved() {
        return linesRemoved;

    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);

    }

    public int getScoreBonus() {
        return scoreBonus;

    }
}*/
