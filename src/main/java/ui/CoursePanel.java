package ui;

import model.Course;
import dao.CourseDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class CoursePanel extends JPanel {
    private JTable courseTable;
    private CourseTableModel tableModel;

    public CoursePanel() {
        setLayout(new BorderLayout());
        initComponents();
        loadCourses();
    }

    private void initComponents() {
        // 工具栏
        JPanel toolPanel = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("新增");
        JButton btnEdit = new JButton("编辑");
        JButton btnDelete = new JButton("删除");
        toolPanel.add(btnAdd);
        toolPanel.add(btnEdit);
        toolPanel.add(btnDelete);

        // 课程表格
        tableModel = new CourseTableModel();
        courseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);

        add(toolPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 事件监听
        btnAdd.addActionListener(this::addCourse);
        btnDelete.addActionListener(this::deleteCourse);
    }

    private void loadCourses() {
        try {
            tableModel.setCourses(CourseDAO.getAllCourses());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载失败: " + e.getMessage());
        }
    }

    private void addCourse(ActionEvent e) {
        // 实现新增课程对话框
    }

    private void deleteCourse(ActionEvent e) {
        int row = courseTable.getSelectedRow();
        if (row == -1) return;
        Course course = tableModel.getCourseAt(row);
        try {
            CourseDAO.deleteCourse(course.getCourseId());
            loadCourses();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "删除失败: " + ex.getMessage());
        }
    }

    // 表格模型
    private static class CourseTableModel extends AbstractTableModel {
        private List<Course> courses = new ArrayList<>();
        private String[] columns = {"课程ID", "课程名称", "学年"};

        public void setCourses(List<Course> courses) {
            this.courses = courses;
            fireTableDataChanged();
        }

        public Course getCourseAt(int row) { return courses.get(row); }

        @Override
        public int getRowCount() { return courses.size(); }

        @Override
        public int getColumnCount() { return columns.length; }

        @Override
        public Object getValueAt(int row, int col) {
            Course course = courses.get(row);
            switch (col) {
                case 0: return course.getCourseId();
                case 1: return course.getCourseName();
                case 2: return course.getAcademicYear();
                default: return null;
            }
        }

        @Override
        public String getColumnName(int col) { return columns[col]; }
    }
}