import java.util.Random;

public class SudokuSolver1 {

    public static final int matrixSize = 9;
    public static final int subMatrixSize = 3;
    public static int validRow = 0;
    public static int validCol = 0;

    public int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    public boolean SolveSudoku(int[][] values, int forbiddenNum) {

        if (!findUnassignedLocation(values)) return true;

        shuffleNums();

        for (int i = 0; i < matrixSize; i++) {

            int num = nums[i];

            if (num == forbiddenNum) continue; //

            if (isSafe(values, validRow, validCol, num)) {
                values[validRow][validCol] = num;

                if (SolveSudoku(values, forbiddenNum)) return true;

                if (validCol == 0) {
                    validRow--;
                    validCol = 8;
                } else {
                    validCol--;
                }

                values[validRow][validCol] = 0;
            }

        }


        return false;

    }

    public boolean createValidSudoku(int[][] values) {

        if (!findUnassignedLocation(values)) return true;

        shuffleNums();

        for (int i = 0; i < matrixSize; i++) {

            int num = nums[i];

            if (isSafe(values, validRow, validCol, num)) {
                values[validRow][validCol] = num;

                if (createValidSudoku(values)) return true;

                if (validCol == 0) {
                    validRow--;
                    validCol = 8;
                } else {
                    validCol--;
                }

                values[validRow][validCol] = 0;
            }

        }


        return false;
    }

    public void shuffleNums() {

        Random random = new Random();
        for (int i = nums.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);

            int a = nums[index];
            nums[index] = nums[i];
            nums[i] = a;
        }

    }

    public boolean findUnassignedLocation(int[][] values) {
        for (int row = 0; row < matrixSize; row++) {
            for (int col = 0; col < matrixSize; col++) {
                if (values[row][col] == 0) {
                    validRow = row;
                    validCol = col;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean usedInRow(int[][] values, int row, int num) {
        for (int col = 0; col < matrixSize; col++) {
            if (values[row][col] == num) return true;
        }

        return false;
    }

    public boolean usedInCol(int[][] values, int col, int num) {
        for (int row = 0; row < matrixSize; row++) {
            if (values[row][col] == num) return true;
        }

        return false;
    }

    public boolean usedInBox(int[][] values, int boxStartRow, int boxStartCol, int num) {
        for (int row = 0; row < subMatrixSize; row++) {
            for (int col = 0; col < subMatrixSize; col++) {
                if (values[row + boxStartRow][col + boxStartCol] == num) return true;
            }
        }

        return false;
    }

    public boolean isSafe(int[][] values, int row, int col, int num) {
        return !usedInRow(values, row, num) &&
                !usedInCol(values, col, num) &&
                !usedInBox(values, row - row % 3, col - col % 3, num);
    }


}