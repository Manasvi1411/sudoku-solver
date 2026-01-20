import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class HistoryWindow extends JFrame {
    private int userId;
    private SudokuFrontend frontend;
    private JTable table;
    private DefaultTableModel model;

    public HistoryWindow(int userId, SudokuFrontend frontend) {
        this.userId = userId;
        this.frontend = frontend;

        setTitle("Your Sudoku History");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"ID", "Date Solved"}, 0);
        table = new JTable(model);
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton viewBtn = new JButton("View Puzzle");
        JButton loadBtn = new JButton("Load Puzzle");
        JPanel btnPanel = new JPanel();
        btnPanel.add(viewBtn);
        btnPanel.add(loadBtn);
        add(btnPanel, BorderLayout.SOUTH);

        loadHistory();

        viewBtn.addActionListener(e -> viewPuzzle());
        loadBtn.addActionListener(e -> loadPuzzle());
        setVisible(true);
    }

    private void loadHistory() {
        ArrayList<String[]> history = SudokuDB.fetchUserHistory(userId);
        model.setRowCount(0);
        for (String[] row : history)
            model.addRow(new Object[]{row[0], row[3]});
    }

    private void viewPuzzle() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        ArrayList<String[]> history = SudokuDB.fetchUserHistory(userId);
        String[] data = history.get(row);
        new PuzzleViewer(stringToBoard(data[1]), stringToBoard(data[2]));
    }

    private void loadPuzzle() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        ArrayList<String[]> history = SudokuDB.fetchUserHistory(userId);
        String[] data = history.get(row);
        frontend.loadPuzzle(stringToBoard(data[1]));
        dispose();
    }

    private int[][] stringToBoard(String s) {
        int[][] board = new int[9][9];
        for (int i = 0; i < 81; i++) {
            board[i / 9][i % 9] = Character.getNumericValue(s.charAt(i));
        }
        return board;
    }
}
