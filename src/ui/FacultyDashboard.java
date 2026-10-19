package ui;

import dao.ProjectDAO;
import dao.SkillDAO;
import dao.StudentDAO;
import model.Project;
import model.Skill;
import model.Student;
import model.User;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.ArrayList;

public class FacultyDashboard extends JFrame {
    private final User faculty;
    private final StudentDAO studentDAO = new StudentDAO();
    private final SkillDAO skillDAO = new SkillDAO();
    private final ProjectDAO projectDAO = new ProjectDAO();

    private final JTextField searchNameField = new JTextField(15);
    private final JTextField departmentField = new JTextField(15);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Admission No", "Name", "Department", "Semester", "Email"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable studentTable = new JTable(tableModel);
    private ArrayList<Student> students = new ArrayList<>();

    public FacultyDashboard(User faculty) {
        this.faculty = faculty;

        setTitle("Student Skill Passport - Faculty Dashboard");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createStudentPanel(), BorderLayout.CENTER);
        add(mainPanel);

        loadStudents("", "");
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Faculty Dashboard - " + faculty.getName());
        titleLabel.setFont(titleLabel.getFont().deriveFont(18f));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(event -> logout());
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(logoutButton, BorderLayout.EAST);
        return panel;
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Name:"));
        searchPanel.add(searchNameField);
        searchPanel.add(new JLabel("Department:"));
        searchPanel.add(departmentField);

        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");
        JButton viewButton = new JButton("View Student");
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);
        searchPanel.add(viewButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setFillsViewportHeight(true);
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        searchButton.addActionListener(event -> loadStudents(
                searchNameField.getText().trim(), departmentField.getText().trim()));
        resetButton.addActionListener(event -> resetSearch());
        viewButton.addActionListener(event -> viewStudent());
        return panel;
    }

    private void loadStudents(String name, String department) {
        try {
            students = studentDAO.searchStudents(name, department);
            tableModel.setRowCount(0);
            for (Student student : students) {
                tableModel.addRow(new Object[]{
                        student.getAdmissionNo(),
                        student.getName(),
                        student.getDepartment(),
                        student.getSemester(),
                        student.getEmail()
                });
            }
        } catch (SQLException exception) {
            showDatabaseError("Could not load students.", exception);
        }
    }

    private void resetSearch() {
        searchNameField.setText("");
        departmentField.setText("");
        loadStudents("", "");
    }

    private void viewStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a student first.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = students.get(selectedRow);
        try {
            ArrayList<Skill> skills = skillDAO.getSkillsByStudentId(student.getProfileId());
            ArrayList<Project> projects = projectDAO.getProjectsByStudentId(student.getProfileId());

            StringBuilder details = new StringBuilder();
            details.append("PROFILE\n");
            details.append("Name: ").append(student.getName()).append("\n");
            details.append("Email: ").append(student.getEmail()).append("\n");
            details.append("Admission No: ").append(student.getAdmissionNo()).append("\n");
            details.append("Department: ").append(student.getDepartment()).append("\n");
            details.append("Semester: ").append(student.getSemester()).append("\n");
            details.append("Phone: ").append(student.getPhone()).append("\n\n");

            details.append("SKILLS\n");
            if (skills.isEmpty()) {
                details.append("No skills added.\n");
            } else {
                for (Skill skill : skills) {
                    details.append("- ").append(skill.getSkillName())
                            .append(" (").append(skill.getSkillLevel()).append(")\n");
                }
            }

            details.append("\nPROJECTS\n");
            if (projects.isEmpty()) {
                details.append("No projects added.\n");
            } else {
                for (Project project : projects) {
                    details.append("- ").append(project.getTitle());
                    if (!project.getTechnology().isEmpty()) {
                        details.append(" [").append(project.getTechnology()).append("]");
                    }
                    details.append("\n  ").append(project.getDescription()).append("\n");
                }
            }

            JTextArea detailsArea = new JTextArea(details.toString(), 20, 55);
            detailsArea.setEditable(false);
            detailsArea.setLineWrap(true);
            detailsArea.setWrapStyleWord(true);
            detailsArea.setCaretPosition(0);
            JOptionPane.showMessageDialog(this, new JScrollPane(detailsArea),
                    "Student Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException exception) {
            showDatabaseError("Could not load student details.", exception);
        }
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }

    private void showDatabaseError(String message, SQLException exception) {
        JOptionPane.showMessageDialog(this, message + "\n" + exception.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
