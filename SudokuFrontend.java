import javax.swing.*;
import java.awt.*;

public class SudokuFrontend {
    private JFrame frame;
    private JTextField[][] cells = new JTextField[9][9];
    private int userId;

    public SudokuFrontend(int userId) {
        this.userId = userId;
        frame = new JFrame("Sudoku Solver - User " + userId);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 750);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("SUDOKU SOLVER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 20));
                int top = (i % 3 == 0) ? 3 : 1;
                int left = (j % 3 == 0) ? 3 : 1;
                int bottom = (i == 8) ? 3 : 1;
                int right = (j == 8) ? 3 : 1;
                cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
                cells[i][j] = cell;
                gridPanel.add(cell);
            }

        JButton solveButton = new JButton("Solve");
        JButton resetButton = new JButton("Reset");
        JButton historyButton = new JButton("History");

        JPanel btnPanel = new JPanel();
        btnPanel.add(solveButton);
        btnPanel.add(resetButton);
        btnPanel.add(historyButton);

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(btnPanel, BorderLayout.SOUTH);

        solveButton.addActionListener(e -> solve());
        resetButton.addActionListener(e -> clear());
        historyButton.addActionListener(e -> new HistoryWindow(userId, this));

        frame.setVisible(true);
    }
    private boolean isValidSudoku(int[][] board) {
    // Check rows
    for (int i = 0; i < 9; i++) {
        boolean[] seen = new boolean[10];
        for (int j = 0; j < 9; j++) {
            int num = board[i][j];
            if (num != 0) {
                if (seen[num]) return false;
                seen[num] = true;
            }
        }
    }

    // Check columns
    for (int j = 0; j < 9; j++) {
        boolean[] seen = new boolean[10];
        for (int i = 0; i < 9; i++) {
            int num = board[i][j];
            if (num != 0) {
                if (seen[num]) return false;
                seen[num] = true;
            }
        }
    }

    // Check 3×3 blocks
    for (int block = 0; block < 9; block++) {
        boolean[] seen = new boolean[10];
        int rowStart = (block / 3) * 3;
        int colStart = (block % 3) * 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = board[rowStart + i][colStart + j];
                if (num != 0) {
                    if (seen[num]) return false;
                    seen[num] = true;
                }
            }
        }
    }

    return true;
}

    private void solve() {
        int[][] question = new int[9][9];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText().trim();
                question[i][j] = text.isEmpty() ? 0 : Integer.parseInt(text);
            }
        // VALIDATION STEP
if (!isValidSudoku(question)) {
    JOptionPane.showMessageDialog(frame, "Invalid Sudoku input!");
    return;
}

        SudokuSolver solver = new SudokuSolver();
        int[][] solved = copy(question);
        if (solver.solveSudoku(solved)) {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    cells[i][j].setText(String.valueOf(solved[i][j]));
            SudokuDB.savePuzzle(userId, question, solved);
            JOptionPane.showMessageDialog(frame, "Solved and saved!");
        } else {
            JOptionPane.showMessageDialog(frame, "No solution exists.");
        }
    }

    private void clear() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                cells[i][j].setText("");
    }

    public void loadPuzzle(int[][] puzzle) {
        clear();
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (puzzle[i][j] != 0)
                    cells[i][j].setText(String.valueOf(puzzle[i][j]));
    }

    private int[][] copy(int[][] src) {
        int[][] c = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(src[i], 0, c[i], 0, 9);
        return c;
    }
}

