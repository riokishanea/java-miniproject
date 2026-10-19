package ui;

import dao.StudentDAO;
import dao.UserDAO;
import model.Student;
import model.User;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JComboBox<String> roleCombo = new JComboBox<>(
            new String[]{"STUDENT", "FACULTY"});

    public LoginFrame() {
        setTitle("Student Skill Passport - Login");
        setSize(420, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("STUDENT SKILL PASSPORT", JLabel.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleCombo);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(event -> login());
        registerButton.addActionListener(event -> {
            dispose();
            new RegisterFrame().setVisible(true);
        });
        getRootPane().setDefaultButton(loginButton);
        add(mainPanel);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and password are required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            User user = new UserDAO().login(email, password, role);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid email, password, or role.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();
            if ("STUDENT".equals(role)) {
                Student student = new StudentDAO().getStudentByUserId(user.getId());
                if (student == null) {
                    JOptionPane.showMessageDialog(null, "Student profile was not found.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    new LoginFrame().setVisible(true);
                    return;
                }
                new StudentDashboard(student).setVisible(true);
            } else {
                new FacultyDashboard(user).setVisible(true);
            }
        } catch (SQLException exception) {
            showDatabaseError(exception);
        }
    }

    private void showDatabaseError(SQLException exception) {
        JOptionPane.showMessageDialog(this,
                "Could not connect to the database.\n" + exception.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

