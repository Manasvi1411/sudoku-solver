import javax.swing.*;
import java.awt.*;

public class PuzzleViewer extends JFrame {
    public PuzzleViewer(int[][] question, int[][] solved) {
        setTitle("View Puzzle");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.add(createGridPanel(question, "Question"));
        mainPanel.add(createGridPanel(solved, "Solution"));

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createGridPanel(int[][] board, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(label, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(9, 9));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 20));
                cell.setEditable(false);
                cell.setText(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));

                int top = (i % 3 == 0) ? 3 : 1;
                int left = (j % 3 == 0) ? 3 : 1;
                int bottom = (i == 8) ? 3 : 1;
                int right = (j == 8) ? 3 : 1;
                cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
                grid.add(cell);
            }
        }
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }
}
