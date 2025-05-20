package service;

import model.Student;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.FileOutputStream;
import java.util.Map;

public class ReportExporter {
    public static void exportStudentStats(Map<Student, String> stats, String filePath) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("学生缺交统计");

        // 表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("学号");
        headerRow.createCell(1).setCellValue("姓名");
        headerRow.createCell(2).setCellValue("缺交次数");
        headerRow.createCell(3).setCellValue("缺交实验列表");

        // 数据行
        int rowNum = 1;
        for (Map.Entry<Student, String> entry : stats.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey().getStudentId());
            row.createCell(1).setCellValue(entry.getKey().getName());
            String[] parts = entry.getValue().split("\\|");
            row.createCell(2).setCellValue(parts[0].trim());
            row.createCell(3).setCellValue(parts[1].trim());
        }

        // 写入文件
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}