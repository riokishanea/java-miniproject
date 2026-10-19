package dao;

import database.DBConnection;
import model.Student;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    public User login(String email, String password, String role) throws SQLException {
        String sql = "SELECT id, name, email, password, role FROM users "
                + "WHERE email = ? AND password = ? AND role = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, role);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new User(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("email"),
                            result.getString("password"),
                            result.getString("role")
                    );
                }
            }
        }
        return null;
    }

    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    public void registerStudent(Student student) throws SQLException {
        String userSql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, 'STUDENT')";
        String profileSql = "INSERT INTO student_profiles "
                + "(user_id, admission_no, department, semester, phone) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement userStatement = connection.prepareStatement(
                    userSql, Statement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, student.getName());
                userStatement.setString(2, student.getEmail());
                userStatement.setString(3, student.getPassword());
                userStatement.executeUpdate();

                try (ResultSet keys = userStatement.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("Could not create the user account.");
                    }
                    student.setId(keys.getInt(1));
                }

                try (PreparedStatement profileStatement = connection.prepareStatement(profileSql)) {
                    profileStatement.setInt(1, student.getId());
                    profileStatement.setString(2, student.getAdmissionNo());
                    profileStatement.setString(3, student.getDepartment());
                    profileStatement.setInt(4, student.getSemester());
                    profileStatement.setString(5, student.getPhone());
                    profileStatement.executeUpdate();
                }
                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        }
    }
}

