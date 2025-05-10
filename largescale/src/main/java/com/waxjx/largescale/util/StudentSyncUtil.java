package com.waxjx.largescale.util;

import com.waxjx.largescale.model.Student;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * 工具类：用于将学生数据同步到从库（排除主库）
 */
public class StudentSyncUtil {

    /**
     * 将学生数据同步更新到多个数据库（同步 update）
     *
     * @param student       学生对象
     * @param dataSourceMap 所有数据源的 Map，key 是 IP
     * @param masterIp      主数据库的 IP，用于跳过
     */
    //  学生信息修改 同步数据库
    public static void syncStudentUpdate(Student student, Map<String, DataSource> dataSourceMap, String masterIp) {
        if (student == null || dataSourceMap == null || masterIp == null) return;

        for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
            String ip = entry.getKey();
            DataSource ds = entry.getValue();

            // 跳过主库
            if (ip.equals(masterIp)) continue;

            try (Connection conn = ds.getConnection()) {
                PreparedStatement statement = conn.prepareStatement(
                        "UPDATE student SET studentName = ?, studentTelephone = ?, sex = ?, college = ?, " +
                                "administrativeClass = ?, idNumber = ?, email = ?, studentStatus = ?, " +
                                "educationalSystem = ?, enrollmentDate = ?, major = ? WHERE studentId = ?"
                );
                statement.setString(1, student.getStudentname());
                statement.setString(2, student.getStudenttelephone());
                statement.setShort(3, student.getSex());
                statement.setString(4, student.getCollege());
                statement.setString(5, student.getAdministrativeclass());
                statement.setString(6, student.getIdnumber());
                statement.setString(7, student.getEmail());
                statement.setShort(8, student.getStudentstatus());
                statement.setShort(9, student.getEducationalsystem());
                statement.setDate(10, student.getEnrollmentdate());
                statement.setString(11, student.getMajor());
                statement.setString(12, student.getStudentid());

                statement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }
        }
    }

    public static void syncStudentInsert(Student student, Map<String, DataSource> dataSourceMap, String masterIp) {
        if (student == null || dataSourceMap == null || masterIp == null) return;
        for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()){
            String ip = entry.getKey();
            DataSource ds = entry.getValue();

            if (ip.equals(masterIp)) continue;

            try (Connection conn = ds.getConnection()){
                PreparedStatement statement = conn.prepareStatement(
                        "INSERT INTO student (studentId, studentName, studentTelephone, sex, college, administrativeClass, idNumber, email, studentStatus, educationalSystem, enrollmentDate, major) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                statement.setString(1, student.getStudentid());
                statement.setString(2, student.getStudentname());
                statement.setString(3, student.getStudenttelephone());
                statement.setShort(4, student.getSex());
                statement.setString(5, student.getCollege());
                statement.setString(6, student.getAdministrativeclass());
                statement.setString(7, student.getIdnumber());
                statement.setString(8, student.getEmail());
                statement.setShort(9, student.getStudentstatus());
                statement.setShort(10, student.getEducationalsystem());
                statement.setDate(11, student.getEnrollmentdate());
                statement.setString(12, student.getMajor());

                statement.executeUpdate();

            } catch (SQLException e) {
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }
        }
    }


    public static void syncStudentDelete(String studentId, Map<String, DataSource> dataSourceMap, String masterIp) {
        if (studentId == null || dataSourceMap == null || masterIp == null) return;
        for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()){
            String ip = entry.getKey();
            DataSource ds = entry.getValue();

            if (ip.equals(masterIp)) continue;

            try (Connection conn = ds.getConnection()){
                PreparedStatement statement = conn.prepareStatement(
                        "delete from student where studentId = ?"
                );
                statement.setString(1, studentId);
                statement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }
        }
    }


}
