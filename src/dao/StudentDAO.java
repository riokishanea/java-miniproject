package dao;

import database.DBConnection;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentDAO {
    private static final String SELECT_STUDENT =
            "SELECT u.id AS user_id, u.name, u.email, u.password, "
            + "sp.id AS profile_id, sp.admission_no, sp.department, sp.semester, sp.phone "
            + "FROM users u JOIN student_profiles sp ON u.id = sp.user_id ";

    public Student getStudentByUserId(int userId) throws SQLException {
        String sql = SELECT_STUDENT + "WHERE u.id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? createStudent(result) : null;
            }
        }
    }

    public ArrayList<Student> getAllStudents() throws SQLException {
        return searchStudents("", "");
    }

    public ArrayList<Student> searchStudents(String name, String department) throws SQLException {
        String sql = SELECT_STUDENT
                + "WHERE LOWER(u.name) LIKE ? AND LOWER(sp.department) LIKE ? ORDER BY u.name";
        ArrayList<Student> students = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name.toLowerCase() + "%");
            statement.setString(2, "%" + department.toLowerCase() + "%");
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    students.add(createStudent(result));
                }
            }
        }
        return students;
    }

    public void updateStudent(Student student) throws SQLException {
        String userSql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        String profileSql = "UPDATE student_profiles SET admission_no = ?, department = ?, "
                + "semester = ?, phone = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement userStatement = connection.prepareStatement(userSql);
                 PreparedStatement profileStatement = connection.prepareStatement(profileSql)) {
                userStatement.setString(1, student.getName());
                userStatement.setString(2, student.getEmail());
                userStatement.setInt(3, student.getId());
                userStatement.executeUpdate();

                profileStatement.setString(1, student.getAdmissionNo());
                profileStatement.setString(2, student.getDepartment());
                profileStatement.setInt(3, student.getSemester());
                profileStatement.setString(4, student.getPhone());
                profileStatement.setInt(5, student.getProfileId());
                profileStatement.executeUpdate();

                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        }
    }

    private Student createStudent(ResultSet result) throws SQLException {
        return new Student(
                result.getInt("user_id"),
                result.getString("name"),
                result.getString("email"),
                result.getString("password"),
                result.getInt("profile_id"),
                result.getString("admission_no"),
                result.getString("department"),
                result.getInt("semester"),
                result.getString("phone")
        );
    }
}

