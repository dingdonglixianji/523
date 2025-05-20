package model;

public class Student {
    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    private String studentId;
    private String name;
    private String grade;
    private String major;

    public Student(String studentId, String name, String grade, String major) {
        this.studentId = studentId;
        this.name = name;
        this.grade = grade;
        this.major = major;
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
}