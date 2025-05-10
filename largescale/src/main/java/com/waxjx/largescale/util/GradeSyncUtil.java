package com.waxjx.largescale.util;

import com.waxjx.largescale.model.Grades;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GradeSyncUtil {

    public static void GradeSyncUtilUpdate(Grades grades, Map<String, DataSource> dataSourceMap, String masterIp){
        if (grades == null || dataSourceMap == null || masterIp == null){return;}
        for(Map.Entry<String, DataSource> entry: dataSourceMap.entrySet()){
            String ip = entry.getKey();
            DataSource ds = entry.getValue();

            if(ip.equals(masterIp)) continue;

            try (Connection conn = ds.getConnection()){
                PreparedStatement statement = conn.prepareStatement(
                        "update grades set score = ?, enrollmentStatus = ?, gpa = ?, " +
                                "assessmentMethod = ?, remarks = ? where studentId = ? and courseId = ?"
                );
                statement.setDouble(1, grades.getScore());
                statement.setInt(2, grades.getEnrollmentstatus());
                statement.setDouble(3, grades.getGpa());
                statement.setString(4, grades.getAssessmentmethod());
                statement.setString(5, grades.getRemarks());
                statement.setString(6, grades.getStudentid());
                statement.setString(7, grades.getCourseid());

                statement.executeUpdate();

            }catch (SQLException e) {
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }
        }
    }

    public static void GradeSyncUtilInsert(Grades grades, Map<String, DataSource> dataSourceMap, String masterIp){
        if (grades == null || dataSourceMap == null || masterIp == null){return;}
        for(Map.Entry<String, DataSource> entry: dataSourceMap.entrySet()){
            String ip = entry.getKey();
            DataSource ds = entry.getValue();

            if(ip.equals(masterIp)) continue;

            try (Connection conn = ds.getConnection()){
                PreparedStatement statement = conn.prepareStatement(
                        "insert into grades values(?,?,?,?,?,?,?,?)"
                );
                statement.setString(1, grades.getGradeid());
                statement.setString(2, grades.getStudentid());
                statement.setString(3, grades.getCourseid());
                statement.setDouble(4, grades.getScore());
                statement.setInt(5, grades.getEnrollmentstatus());
                statement.setDouble(6, grades.getGpa());
                statement.setString(7, grades.getAssessmentmethod());
                statement.setString(8, grades.getRemarks());

                statement.executeUpdate();

            } catch (SQLException e) {
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }
        }
    }

    public static void GradeSyncUtilDelete(String studentId, String courseId, Map<String, DataSource> dataSourceMap, String masterIp){
        if (studentId == null || courseId == null || dataSourceMap == null || masterIp == null){return;}
        for(Map.Entry<String, DataSource> entry: dataSourceMap.entrySet()){
            String ip = entry.getKey();
            DataSource ds = entry.getValue();
            if(ip.equals(masterIp)) continue;

            try(Connection conn = ds.getConnection()){
                PreparedStatement statement = conn.prepareStatement(
                        "delete from grades where studentId = ? and courseId = ?"
                );
                statement.setString(1, studentId);
                statement.setString(2, courseId);

                statement.executeUpdate();

            } catch (SQLException e) {
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }

        }
    }
}
