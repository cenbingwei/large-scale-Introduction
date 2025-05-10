package com.waxjx.largescale.util;

import com.waxjx.largescale.model.Teachers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class TeacherSyncUtil {

    public static void TeacherSyncUtilUpdate(Teachers teachers, Map<String, DataSource> dataSourceMap, String masterIp){
        if (teachers == null || dataSourceMap == null || masterIp == null){
            return ;
        }

        for(Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()){
            String ip = entry.getKey();
            DataSource ds = entry.getValue();

            // 跳过主库 否则会主库再次修改 出现锁超时
            if(ip.equals(masterIp)) continue;

            try(Connection conn = ds.getConnection()){
                PreparedStatement statement = conn.prepareStatement(
                        "update teachers set name = ?, sex = ?, birthDate = ?, title = ?, " +
                                "telephone = ?, remarks = ? where teacherID = ?"
                );
                statement.setString(1, teachers.getName());
                statement.setInt(2, teachers.getSex());
                statement.setDate(3, teachers.getBirthdate());
                statement.setString(4, teachers.getTitle());
                statement.setString(5, teachers.getTelephone());
                statement.setString(6, teachers.getRemarks());
                statement.setString(7, teachers.getTeacherid());

                statement.executeUpdate();

            } catch (SQLException e) {
                // throw new RuntimeException(e);
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }
        }
    }

    public static void TeacherSyncUtilInsert(Teachers teachers, Map<String, DataSource> dataSourceMap, String masterIp){
        if (teachers == null || dataSourceMap == null || masterIp == null){return;}
        for(Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()){
            String ip = entry.getKey();
            DataSource ds = entry.getValue();

            // 跳过主库 否则会主库再次修改 出现锁超时
            if(ip.equals(masterIp)) continue;

            try(Connection conn = ds.getConnection()){
                PreparedStatement statement = conn.prepareStatement(
                        "insert into teachers values (?, ?, ?, ?, ?, ?, ?)"
                );
                statement.setString(1, teachers.getTeacherid());
                statement.setString(2, teachers.getName());
                statement.setInt(3, teachers.getSex());
                statement.setDate(4, teachers.getBirthdate());
                statement.setString(5, teachers.getTitle());
                statement.setString(6, teachers.getTelephone());
                statement.setString(7, teachers.getRemarks());

                statement.executeUpdate();

            } catch (SQLException e) {
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }
        }
    }

    public static void TeacherSyncUtilDelete(String teacherId, Map<String, DataSource> dataSourceMap, String masterIp){
        if (teacherId == null || dataSourceMap == null || masterIp == null){return;}
        for(Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()){
            String ip = entry.getKey();
            DataSource ds = entry.getValue();
            if(ip.equals(masterIp)) continue;

            try(Connection conn = ds.getConnection()){
                PreparedStatement statement = conn.prepareStatement(
                        "delete from teachers where teacherID = ?"
                );
                statement.setString(1, teacherId);

                statement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }
        }
    }

}
