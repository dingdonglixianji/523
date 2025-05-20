package dao;

import model.Student;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public static void importStudents(List<Student> students) {
        String sql = "INSERT INTO Student (student_id, name, grade, major) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Student student : students) {
                stmt.setString(1, student.getStudentId());
                stmt.setString(2, student.getName());
                stmt.setString(3, student.getGrade());
                stmt.setString(4, student.getMajor());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("导入学生数据失败", e); // 直接抛出非受检异常
        }
    }

    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getString("student_id"),
                        rs.getString("name"),
                        rs.getString("grade"),
                        rs.getString("major")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询学生列表失败", e); // 直接抛出非受检异常
        }
        return students;
    }
}