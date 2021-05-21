public class SudokuSolver2 {

    static int N = 9;

    static boolean SolveSuduko(int matrix[][], int row,
                               int col) {

        if (row == N - 1 && col == N)

            return true;


        if (col == N) {
            row++;
            col = 0;
        }

        if (matrix[row][col] != 0)
            return SolveSuduko(matrix, row, col + 1);

        for (int num = 1; num < 10; num++) {

            if (isSafe(matrix, row, col, num)) {

                matrix[row][col] = num;

                if (SolveSuduko(matrix, row, col + 1))
                    return true;
            }

            matrix[row][col] = 0;
        }
        return false;
    }


    static boolean isSafe(int[][] matrix, int row, int col,
                          int num) {

        for (int x = 0; x <= 8; x++)
            if (matrix[row][x] == num)
                return false;

        for (int x = 0; x <= 8; x++)
            if (matrix[x][col] == num)
                return false;

        int startRow = row - row % 3, startCol
                = col - col % 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (matrix[i + startRow][j + startCol] == num)
                    return false;

        return true;
    }

}

