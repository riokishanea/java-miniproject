package ui;

import dao.UserDAO;
import model.Student;

import javax.swing.JButton;
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

public class RegisterFrame extends JFrame {
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JTextField admissionField = new JTextField();
    private final JTextField departmentField = new JTextField();
    private final JTextField semesterField = new JTextField();
    private final JTextField phoneField = new JTextField();

    public RegisterFrame() {
        setTitle("Student Registration");
        setSize(480, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Student Registration", JLabel.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        addField(formPanel, "Name:", nameField);
        addField(formPanel, "Email:", emailField);
        addField(formPanel, "Password:", passwordField);
        addField(formPanel, "Admission Number:", admissionField);
        addField(formPanel, "Department:", departmentField);
        addField(formPanel, "Semester:", semesterField);
        addField(formPanel, "Phone:", phoneField);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        registerButton.addActionListener(event -> register());
        backButton.addActionListener(event -> openLogin());
        getRootPane().setDefaultButton(registerButton);
        add(mainPanel);
    }

    private void addField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    private void register() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String admissionNo = admissionField.getText().trim();
        String department = departmentField.getText().trim();
        String semesterText = semesterField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()
                || admissionNo.isEmpty() || department.isEmpty() || semesterText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int semester;
        try {
            semester = Integer.parseInt(semesterText);
            if (semester < 1 || semester > 12) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Semester must be a number from 1 to 12.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            if (userDAO.isEmailExists(email)) {
                JOptionPane.showMessageDialog(this, "This email is already registered.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Student student = new Student(0, name, email, password, 0,
                    admissionNo, department, semester, phone);
            userDAO.registerStudent(student);
            JOptionPane.showMessageDialog(this, "Registration successful. Please log in.");
            openLogin();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this,
                    "Registration failed. Check that the email and admission number are unique.\n"
                            + exception.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openLogin() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
