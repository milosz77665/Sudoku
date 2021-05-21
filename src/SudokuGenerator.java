import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SudokuGenerator {

    int[][] sudoku = new int[9][9];
    int[][] fsudoku = new int[9][9];
    static ArrayList<int[][]> list = new ArrayList<>();

    public void generateSudoku() {
        SudokuSolver1 sudokuSolver1 = new SudokuSolver1();
        sudokuSolver1.createValidSudoku(sudoku);

        int count = 0;

        int infloop = 0;
        while (count < 40) {
            Random random = new Random();
            infloop++;
            int row = 0;
            int col = 0;
            if (count < 15) {
                row = random.nextInt(3);
                col = random.nextInt(9);
            } else if (infloop > 400) {
                SudokuGenerator gen1 = new SudokuGenerator();
                list.clear();
                list = gen1.SudokuLoop();
                break;
            } else if (count >= 15 && count < 30) {
                row = random.nextInt(3) + 3;
                col = random.nextInt(9);
            } else {
                row = random.nextInt(3) + 6;
                col = random.nextInt(9);

            }
            int num = sudoku[row][col];
            int tempValues[][] = Arrays.copyOf(sudoku, sudoku.length);
            if (sudoku[row][col] != 0) {
                sudoku[row][col] = 0;
            } else {
                continue;
            }
            if (sudokuSolver1.SolveSudoku(tempValues, num)) {
                sudoku[row][col] = num;
                continue;
            }
            count++;
        }
        copy(sudoku);

    }

    private void copy(int[][] matrix) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                fsudoku[i][j] = matrix[i][j];
            }
        }
        list.add(fsudoku);
    }

    private void printNums(int[][] values) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print("\t" + values[i][j]);
            }
            System.out.println();
        }
    }


    public ArrayList SudokuLoop() {
        int done = 0;
        do {
            generateSudoku();
            SudokuSolver2 sudokuSolver2 = new SudokuSolver2();
            if (sudokuSolver2.SolveSuduko(sudoku, 0, 0)) {
                break;
            } else {
                SudokuGenerator gen2 = new SudokuGenerator();
                list.clear();
                list = gen2.SudokuLoop();
                break;

            }
        } while (done == 0);
        {
        }

        list.add(sudoku);
        return list;
    }


    public static void main(String[] args) {
        SudokuGenerator gen = new SudokuGenerator();
        ArrayList<int[][]> list;
        list = gen.SudokuLoop();
        System.out.println("----------------pusta-----");
        gen.printNums(list.get(0));
        System.out.println("---------------pełna------");
        gen.printNums(list.get(1));

    }
}