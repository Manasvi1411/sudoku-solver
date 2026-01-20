import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginPage() {
        setTitle("Login - Sudoku Solver");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        userField = new JTextField();
        passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        add(userLabel); add(userField);
        add(passLabel); add(passField);
        add(loginBtn); add(registerBtn);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void login() {
        String u = userField.getText();
        String p = new String(passField.getPassword());
        int id = SudokuDB.login(u, p);
        if (id > 0) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            new SudokuFrontend(id);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!");
        }
    }

    private void register() {
        String u = userField.getText();
        String p = new String(passField.getPassword());
        if (SudokuDB.register(u, p))
            JOptionPane.showMessageDialog(this, "Registration successful!");
        else
            JOptionPane.showMessageDialog(this, "Username taken or error!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
