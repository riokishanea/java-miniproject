package model;

public class Skill {
    private int id;
    private int studentId;
    private String skillName;
    private String skillLevel;

    public Skill() {
    }

    public Skill(int id, int studentId, String skillName, String skillLevel) {
        this.id = id;
        this.studentId = studentId;
        this.skillName = skillName;
        this.skillLevel = skillLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }
}

