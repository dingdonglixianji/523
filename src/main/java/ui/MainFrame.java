package ui;

import model.Student;
import service.AnalysisService;
import service.ExcelParser;
import service.ReportExporter;
import service.ReportParser;
import dao.StudentDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;
import java.util.Set;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextArea resultArea = new JTextArea();
    private Map<Student, String> studentStats;
    private Map<Integer, String> experimentStats;

    public MainFrame() {
        setTitle("ERAT v1.0");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    //===================== 初始化界面 =====================//
    private void initUI() {
        // 主布局
        setLayout(new BorderLayout());

        // 工具栏
        JToolBar toolBar = new JToolBar();
        addButtonsToToolbar(toolBar);
        add(toolBar, BorderLayout.NORTH);

        // 标签页（课程管理 + 实验统计）
        tabbedPane.addTab("实验统计", createAnalysisPanel());
        tabbedPane.addTab("课程管理", new CoursePanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    //===================== 组件初始化 =====================//
    private JPanel createAnalysisPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        resultArea.setEditable(false);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        return panel;
    }

    private void addButtonsToToolbar(JToolBar toolBar) {
        JButton btnImportStudents = new JButton("导入学生名单");
        JButton btnAnalyze = new JButton("分析实验报告");
        JButton btnExport = new JButton("导出结果");
        JButton btnChart = new JButton("查看图表");

        // 绑定事件
        btnImportStudents.addActionListener(this::importStudents);
        btnAnalyze.addActionListener(this::analyzeReports);
        btnExport.addActionListener(this::exportResults);
        btnChart.addActionListener(this::showChart);

        // 添加按钮
        toolBar.add(btnImportStudents);
        toolBar.add(btnAnalyze);
        toolBar.add(btnExport);
        toolBar.add(btnChart);
    }

    //===================== 核心功能实现 =====================//

    // 1. 导入学生名单
    private void importStudents(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File excelFile = chooser.getSelectedFile();
                java.util.List<Student> students = ExcelParser.parseStudentList(excelFile);
                dao.StudentDAO.importStudents(students); // 调用DAO
                JOptionPane.showMessageDialog(this, "成功导入 " + students.size() + " 名学生");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "导入失败: " + ex.getMessage());
            }
        }
    }

    // 2. 分析实验报告
    private void analyzeReports(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File dir = chooser.getSelectedFile();
                Map<String, Set<Integer>> missingMap = new ReportParser().parseReports(dir);

                // 生成统计结果
                studentStats = AnalysisService.generateStudentStats(missingMap);
                experimentStats = AnalysisService.generateExperimentStats(missingMap);

                // 更新结果展示区
                updateResultArea();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "分析失败: " + ex.getMessage());
            }
        }
    }

    // 3. 导出结果
    private void exportResults(ActionEvent e) {
        if (studentStats == null || experimentStats == null) {
            JOptionPane.showMessageDialog(this, "请先分析实验报告");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("统计结果.xlsx"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                ReportExporter.exportStudentStats(studentStats, chooser.getSelectedFile().getPath());
                JOptionPane.showMessageDialog(this, "导出成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "导出失败: " + ex.getMessage());
            }
        }
    }

    // 4. 查看图表
    private void showChart(ActionEvent e) {
        if (experimentStats == null) {
            JOptionPane.showMessageDialog(this, "请先分析实验报告");
            return;
        }
        try {
            DefaultCategoryDataset dataset = AnalysisService.generateChartDataset(experimentStats);
            JFreeChart chart = ChartFactory.createLineChart(
                    "实验提交率统计", "实验编号", "提交率 (%)", dataset
            );
            ChartFrame frame = new ChartFrame("统计图表", chart);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "生成图表失败: " + ex.getMessage());
        }
    }

    //===================== 辅助方法 =====================//
    private void updateResultArea() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== 学生缺交统计 ===\n");
        studentStats.forEach((student, stat) ->
                sb.append(student.getStudentId()).append("\t")
                        .append(student.getName()).append("\t")
                        .append(stat).append("\n"));

        sb.append("\n=== 实验缺交统计 ===\n");
        experimentStats.forEach((expId, stat) ->
                sb.append("实验").append(expId).append("\t")
                        .append(stat).append("\n"));

        resultArea.setText(sb.toString());
    }

    //===================== 启动入口 =====================//
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}