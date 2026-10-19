package model;

public class Student extends User {
    private int profileId;
    private String admissionNo;
    private String department;
    private int semester;
    private String phone;

    public Student() {
        setRole("STUDENT");
    }

    public Student(int userId, String name, String email, String password,
                   int profileId, String admissionNo, String department,
                   int semester, String phone) {
        super(userId, name, email, password, "STUDENT");
        this.profileId = profileId;
        this.admissionNo = admissionNo;
        this.department = department;
        this.semester = semester;
        this.phone = phone;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

