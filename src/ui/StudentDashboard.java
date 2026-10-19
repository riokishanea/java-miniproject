package ui;

import dao.ProjectDAO;
import dao.SkillDAO;
import dao.StudentDAO;
import model.Project;
import model.Skill;
import model.Student;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentDashboard extends JFrame {
    private final Student student;
    private final StudentDAO studentDAO = new StudentDAO();
    private final SkillDAO skillDAO = new SkillDAO();
    private final ProjectDAO projectDAO = new ProjectDAO();

    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField admissionField = new JTextField();
    private final JTextField departmentField = new JTextField();
    private final JTextField semesterField = new JTextField();
    private final JTextField phoneField = new JTextField();

    private final DefaultTableModel skillTableModel = readOnlyModel(
            new String[]{"Skill Name", "Skill Level"});
    private final JTable skillTable = new JTable(skillTableModel);
    private final JTextField skillNameField = new JTextField(16);
    private final JComboBox<String> skillLevelCombo = new JComboBox<>(
            new String[]{"Beginner", "Intermediate", "Advanced"});
    private ArrayList<Skill> skills = new ArrayList<>();

    private final DefaultTableModel projectTableModel = readOnlyModel(
            new String[]{"Title", "Technology", "Description"});
    private final JTable projectTable = new JTable(projectTableModel);
    private final JTextField projectTitleField = new JTextField(12);
    private final JTextField technologyField = new JTextField(12);
    private final JTextField descriptionField = new JTextField(18);
    private ArrayList<Project> projects = new ArrayList<>();

    public StudentDashboard(Student student) {
        this.student = student;

        setTitle("Student Skill Passport - Student Dashboard");
        setSize(850, 570);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        mainPanel.add(createHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Profile", createProfilePanel());
        tabs.addTab("Skills", createSkillsPanel());
        tabs.addTab("Projects", createProjectsPanel());
        mainPanel.add(tabs, BorderLayout.CENTER);

        add(mainPanel);
        loadProfileFields();
        loadSkills();
        loadProjects();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName());
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(18f));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(event -> logout());
        panel.add(welcomeLabel, BorderLayout.WEST);
        panel.add(logoutButton, BorderLayout.EAST);
        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(20, 100, 20, 100));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        addField(formPanel, "Name:", nameField);
        addField(formPanel, "Email:", emailField);
        addField(formPanel, "Admission Number:", admissionField);
        addField(formPanel, "Department:", departmentField);
        addField(formPanel, "Semester:", semesterField);
        addField(formPanel, "Phone:", phoneField);
        panel.add(formPanel, BorderLayout.CENTER);

        JButton updateButton = new JButton("Update Profile");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        updateButton.addActionListener(event -> updateProfile());
        return panel;
    }

    private JPanel createSkillsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 10, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Skill Name:"));
        inputPanel.add(skillNameField);
        inputPanel.add(new JLabel("Level:"));
        inputPanel.add(skillLevelCombo);

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        panel.add(inputPanel, BorderLayout.NORTH);

        skillTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        skillTable.setFillsViewportHeight(true);
        panel.add(new JScrollPane(skillTable), BorderLayout.CENTER);

        addButton.addActionListener(event -> addSkill());
        deleteButton.addActionListener(event -> deleteSkill());
        return panel;
    }

    private JPanel createProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 10, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(projectTitleField);
        inputPanel.add(new JLabel("Technology:"));
        inputPanel.add(technologyField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        panel.add(inputPanel, BorderLayout.NORTH);

        projectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectTable.setFillsViewportHeight(true);
        panel.add(new JScrollPane(projectTable), BorderLayout.CENTER);

        addButton.addActionListener(event -> addProject());
        deleteButton.addActionListener(event -> deleteProject());
        return panel;
    }

    private void addField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }

    private void loadProfileFields() {
        nameField.setText(student.getName());
        emailField.setText(student.getEmail());
        admissionField.setText(student.getAdmissionNo());
        departmentField.setText(student.getDepartment());
        semesterField.setText(String.valueOf(student.getSemester()));
        phoneField.setText(student.getPhone());
    }

    private void updateProfile() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String admissionNo = admissionField.getText().trim();
        String department = departmentField.getText().trim();
        String semesterText = semesterField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || admissionNo.isEmpty()
                || department.isEmpty() || semesterText.isEmpty()) {
            showValidation("Please fill all required profile fields.");
            return;
        }

        int semester;
        try {
            semester = Integer.parseInt(semesterText);
            if (semester < 1 || semester > 12) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            showValidation("Semester must be a number from 1 to 12.");
            return;
        }

        student.setName(name);
        student.setEmail(email);
        student.setAdmissionNo(admissionNo);
        student.setDepartment(department);
        student.setSemester(semester);
        student.setPhone(phone);

        try {
            studentDAO.updateStudent(student);
            JOptionPane.showMessageDialog(this, "Profile updated successfully.");
        } catch (SQLException exception) {
            showDatabaseError("Profile update failed. Email and admission number must be unique.",
                    exception);
        }
    }

    private void loadSkills() {
        try {
            skills = skillDAO.getSkillsByStudentId(student.getProfileId());
            skillTableModel.setRowCount(0);
            for (Skill skill : skills) {
                skillTableModel.addRow(new Object[]{skill.getSkillName(), skill.getSkillLevel()});
            }
        } catch (SQLException exception) {
            showDatabaseError("Could not load skills.", exception);
        }
    }

    private void addSkill() {
        String skillName = skillNameField.getText().trim();
        if (skillName.isEmpty()) {
            showValidation("Skill name cannot be empty.");
            return;
        }

        Skill skill = new Skill(0, student.getProfileId(), skillName,
                (String) skillLevelCombo.getSelectedItem());
        try {
            skillDAO.addSkill(skill);
            skillNameField.setText("");
            loadSkills();
        } catch (SQLException exception) {
            showDatabaseError("Could not add the skill.", exception);
        }
    }

    private void deleteSkill() {
        int selectedRow = skillTable.getSelectedRow();
        if (selectedRow < 0) {
            showValidation("Select a skill to delete.");
            return;
        }

        try {
            skillDAO.deleteSkill(skills.get(selectedRow).getId(), student.getProfileId());
            loadSkills();
        } catch (SQLException exception) {
            showDatabaseError("Could not delete the skill.", exception);
        }
    }

    private void loadProjects() {
        try {
            projects = projectDAO.getProjectsByStudentId(student.getProfileId());
            projectTableModel.setRowCount(0);
            for (Project project : projects) {
                projectTableModel.addRow(new Object[]{
                        project.getTitle(), project.getTechnology(), project.getDescription()
                });
            }
        } catch (SQLException exception) {
            showDatabaseError("Could not load projects.", exception);
        }
    }

    private void addProject() {
        String title = projectTitleField.getText().trim();
        String technology = technologyField.getText().trim();
        String description = descriptionField.getText().trim();
        if (title.isEmpty()) {
            showValidation("Project title cannot be empty.");
            return;
        }

        Project project = new Project(0, student.getProfileId(), title, technology, description);
        try {
            projectDAO.addProject(project);
            projectTitleField.setText("");
            technologyField.setText("");
            descriptionField.setText("");
            loadProjects();
        } catch (SQLException exception) {
            showDatabaseError("Could not add the project.", exception);
        }
    }

    private void deleteProject() {
        int selectedRow = projectTable.getSelectedRow();
        if (selectedRow < 0) {
            showValidation("Select a project to delete.");
            return;
        }

        try {
            projectDAO.deleteProject(projects.get(selectedRow).getId(), student.getProfileId());
            loadProjects();
        } catch (SQLException exception) {
            showDatabaseError("Could not delete the project.", exception);
        }
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }

    private void showValidation(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation", JOptionPane.WARNING_MESSAGE);
    }

    private void showDatabaseError(String message, SQLException exception) {
        JOptionPane.showMessageDialog(this, message + "\n" + exception.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    private static DefaultTableModel readOnlyModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
