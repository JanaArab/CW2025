/// stores the matrix the includes all data regarding row deletion
/// represents the result of deleting a row ,the updated matrix
/// and score bonus
package com.comp2042.tetris.model.board;


import com.comp2042.tetris.utils.MatrixOperations;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the result of a row clearing operation in Tetris.
 * This record stores information about cleared rows, the updated board matrix,
 * and the score bonus earned from clearing rows.
 *
 * <p>This is implemented as a Java record for immutability and conciseness,
 * as it only contains data with no behavior logic.</p>
 *
 * @param linesRemoved the number of lines that were cleared
 * @param newMatrix the updated board matrix after rows were cleared
 * @param scoreBonus the score bonus earned from clearing the rows
 * @param clearedRows a list of row indices that were cleared
 */
public record ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, List<Integer> clearedRows) {

    /**
     * Returns a defensive copy of the new matrix to prevent external modification.
     *
     * @return a copy of the board matrix after row clearing
     */
    @Override
    public int[][] newMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

}
