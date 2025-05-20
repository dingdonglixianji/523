package model;

import java.util.ArrayList;
import java.util.List;

public class StudentDatabase {
    private static List<Student> students = new ArrayList<>();

    public static void addStudent(Student student) {
        students.add(student);
    }

    public static List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public static void clear() {
        students.clear();
    }
}