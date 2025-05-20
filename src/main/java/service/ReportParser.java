package service;

import model.Student;
import dao.StudentDAO;
import java.io.File;
import java.util.*;

public class ReportParser {
    public Map<String, Set<Integer>> parseReports(File directory) {
        Map<String, Set<Integer>> missingMap = new HashMap<>();
        for (File expDir : directory.listFiles()) {
            if (expDir.isDirectory() && expDir.getName().startsWith("实验")) {
                int expId = Integer.parseInt(expDir.getName().replace("实验", ""));
                Set<String> submittedStudents = new HashSet<>();
                for (File report : expDir.listFiles()) {
                    String[] parts = report.getName().split("_");
                    if (parts.length >= 3) submittedStudents.add(parts[1]);
                }
                for (Student student : StudentDAO.getAllStudents()) {
                    if (!submittedStudents.contains(student.getStudentId())) {
                        missingMap.computeIfAbsent(student.getStudentId(), k -> new HashSet<>()).add(expId);
                    }
                }
            }
        }
        return missingMap;
    }
}