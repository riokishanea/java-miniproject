package dao;

import database.DBConnection;
import model.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectDAO {
    public ArrayList<Project> getProjectsByStudentId(int studentId) throws SQLException {
        String sql = "SELECT id, student_id, title, technology, description "
                + "FROM projects WHERE student_id = ? ORDER BY title";
        ArrayList<Project> projects = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    projects.add(new Project(
                            result.getInt("id"),
                            result.getInt("student_id"),
                            result.getString("title"),
                            result.getString("technology"),
                            result.getString("description")
                    ));
                }
            }
        }
        return projects;
    }

    public void addProject(Project project) throws SQLException {
        String sql = "INSERT INTO projects (student_id, title, technology, description) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, project.getStudentId());
            statement.setString(2, project.getTitle());
            statement.setString(3, project.getTechnology());
            statement.setString(4, project.getDescription());
            statement.executeUpdate();
        }
    }

    public void deleteProject(int projectId, int studentId) throws SQLException {
        String sql = "DELETE FROM projects WHERE id = ? AND student_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, projectId);
            statement.setInt(2, studentId);
            statement.executeUpdate();
        }
    }
}
