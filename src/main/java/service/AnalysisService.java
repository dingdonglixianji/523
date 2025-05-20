package service;

import model.Student;
import dao.StudentDAO;
import java.util.*;
import java.util.stream.Collectors;
import org.jfree.data.category.DefaultCategoryDataset;

public class AnalysisService {

    //===================== 学生维度统计 =====================//
    /**
     * 生成学生缺交统计结果
     * @param missingMap 缺交数据（Key: 学号, Value: 缺交的实验编号集合）
     * @return Map<Student, String>（Key: 学生对象, Value: "缺交次数 | 缺交实验列表"）
     */
    public static Map<Student, String> generateStudentStats(Map<String, Set<Integer>> missingMap) {
        Map<Student, String> studentStats = new LinkedHashMap<>();
        List<Student> allStudents = StudentDAO.getAllStudents(); // 可能抛出 RuntimeException
        for (Student student : allStudents) {
            Set<Integer> missingExps = missingMap.getOrDefault(student.getStudentId(), new HashSet<>());
            String missingList = formatMissingList(missingExps);
            String stat = missingExps.size() + "次缺交 | " + missingList;
            studentStats.put(student, stat);
        }
        return studentStats;
    }

    //===================== 实验维度统计 =====================//
    /**
     * 生成实验缺交统计结果
     * @param missingMap 缺交数据（Key: 学号, Value: 缺交的实验编号集合）
     * @return Map<Integer, String>（Key: 实验编号, Value: "缺交人数 | 缺交学生列表"）
     */
    public static Map<Integer, String> generateExperimentStats(Map<String, Set<Integer>> missingMap) {
        Map<Integer, Set<String>> experimentMissingStudents = new HashMap<>();
        List<Student> allStudents = StudentDAO.getAllStudents(); // 可能抛出 RuntimeException
        for (Student student : allStudents) {
            Set<Integer> missingExps = missingMap.getOrDefault(student.getStudentId(), new HashSet<>());
            for (Integer expId : missingExps) {
                experimentMissingStudents
                        .computeIfAbsent(expId, k -> new HashSet<>())
                        .add(student.getName());
            }
        }

        // 格式化输出
        Map<Integer, String> experimentStats = new TreeMap<>();
        experimentMissingStudents.forEach((expId, names) -> {
            String stat = names.size() + "人缺交 | " + String.join("、", names);
            experimentStats.put(expId, stat);
        });
        return experimentStats;
    }

    //===================== 可视化数据生成 =====================//
    /**
     * 生成折线图数据集（用于 JFreeChart）
     * @param experimentStats 实验维度统计结果
     * @return DefaultCategoryDataset（横轴：实验编号，纵轴：提交率）
     */
    public static DefaultCategoryDataset generateChartDataset(Map<Integer, String> experimentStats) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int totalStudents = StudentDAO.getAllStudents().size(); // 可能抛出 RuntimeException
        experimentStats.forEach((expId, stat) -> {
            int missing = Integer.parseInt(stat.split("\\|")[0].replaceAll("[^0-9]", ""));
            double submissionRate = ((totalStudents - missing) * 100.0) / totalStudents;
            dataset.addValue(submissionRate, "提交率", "实验" + expId);
        });
        return dataset;
    }

    //===================== 工具方法 =====================//
    private static String formatMissingList(Set<Integer> missingExps) {
        return missingExps.stream()
                .sorted()
                .map(expId -> "实验" + expId)
                .collect(Collectors.joining("、"));
    }
}