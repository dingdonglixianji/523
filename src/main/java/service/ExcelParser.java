package service;

import model.Student;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExcelParser {
    public static List<Student> parseStudentList(File excelFile) throws Exception {
        List<Student> students = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // 跳过表头
            String studentId = row.getCell(0).getStringCellValue();
            String name = row.getCell(1).getStringCellValue();
            String grade = row.getCell(2).getStringCellValue();
            String major = row.getCell(3).getStringCellValue();
            students.add(new Student(studentId, name, grade, major));
        }
        workbook.close();
        return students;
    }
}