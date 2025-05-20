package model;

import java.util.HashSet;
import java.util.Set;

public class Experiment {
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public Set<String> getSubmittedStudents() {
        return submittedStudents;
    }

    public void setSubmittedStudents(Set<String> submittedStudents) {
        this.submittedStudents = submittedStudents;
    }

    private String courseId;
    private String className;
    private int experimentId;
    private Set<String> submittedStudents = new HashSet<>();

    public Experiment(String courseId, String className, int experimentId) {
        this.courseId = courseId;
        this.className = className;
        this.experimentId = experimentId;
    }

    public void addSubmittedStudent(String studentId) {
        submittedStudents.add(studentId);
    }
    // 省略其他方法
}