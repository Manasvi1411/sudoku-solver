import java.sql.*;
import java.util.ArrayList;

public class SudokuDB {
    private static final String URL = "jdbc:mysql://localhost:3306/sudoku_app";
    private static final String USER = "root";
    private static final String PASSWORD = "123456789";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Register
    public static boolean register(String username, String password) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users(username, password) VALUES(?, ?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Login
    public static int login(String username, String password) {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT id FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Save puzzle
    public static void savePuzzle(int userId, int[][] question, int[][] solved) {
        StringBuilder q = new StringBuilder(), s = new StringBuilder();
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                q.append(question[i][j]);
                s.append(solved[i][j]);
            }
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO sudoku_history(user_id, user_input, solved_puzzle) VALUES(?, ?, ?)");
            ps.setInt(1, userId);
            ps.setString(2, q.toString());
            ps.setString(3, s.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch user history
    public static ArrayList<String[]> fetchUserHistory(int userId) {
        ArrayList<String[]> list = new ArrayList<>();
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id, user_input, solved_puzzle, solved_at FROM sudoku_history WHERE user_id=? ORDER BY solved_at DESC");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("id"),
                    rs.getString("user_input"),
                    rs.getString("solved_puzzle"),
                    rs.getString("solved_at")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
