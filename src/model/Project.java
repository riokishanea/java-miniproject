package model;

public class Project {
    private int id;
    private int studentId;
    private String title;
    private String technology;
    private String description;

    public Project() {
    }

    public Project(int id, int studentId, String title,
                   String technology, String description) {
        this.id = id;
        this.studentId = studentId;
        this.title = title;
        this.technology = technology;
        this.description = description;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
