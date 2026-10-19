package dao;

import database.DBConnection;
import model.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SkillDAO {
    public ArrayList<Skill> getSkillsByStudentId(int studentId) throws SQLException {
        String sql = "SELECT id, student_id, skill_name, skill_level "
                + "FROM skills WHERE student_id = ? ORDER BY skill_name";
        ArrayList<Skill> skills = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    skills.add(new Skill(
                            result.getInt("id"),
                            result.getInt("student_id"),
                            result.getString("skill_name"),
                            result.getString("skill_level")
                    ));
                }
            }
        }
        return skills;
    }

    public void addSkill(Skill skill) throws SQLException {
        String sql = "INSERT INTO skills (student_id, skill_name, skill_level) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, skill.getStudentId());
            statement.setString(2, skill.getSkillName());
            statement.setString(3, skill.getSkillLevel());
            statement.executeUpdate();
        }
    }

    public void deleteSkill(int skillId, int studentId) throws SQLException {
        String sql = "DELETE FROM skills WHERE id = ? AND student_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, skillId);
            statement.setInt(2, studentId);
            statement.executeUpdate();
        }
    }
}

