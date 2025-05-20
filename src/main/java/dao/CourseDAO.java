package dao;

import model.Course;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    public static List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT course_id, course_name, academic_year FROM Course";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getString("academic_year")
                ));
            }
        }
        return courses;
    }

    public static void addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO Course (course_id, course_name, academic_year) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseId());
            stmt.setString(2, course.getCourseName());
            stmt.setString(3, course.getAcademicYear());
            stmt.executeUpdate();
        }
    }
    public static void deleteCourse(String c) throws SQLException {
    }
}