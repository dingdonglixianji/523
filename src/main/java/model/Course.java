package model;

public class Course {
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    private String courseId;
    private String courseName;
    private String academicYear;

    public Course(String courseId, String courseName, String academicYear) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.academicYear = academicYear;
    }

    // Getters and Setters
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
}