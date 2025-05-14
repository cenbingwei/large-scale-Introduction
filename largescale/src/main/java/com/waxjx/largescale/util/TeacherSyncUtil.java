package com.waxjx.largescale.util;

import com.waxjx.largescale.model.Teachers;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class TeacherSyncUtil {

    public static void TeacherSyncUtilUpdate(Teachers teachers, Map<String, DataSource> dataSourceMap, String masterIp, String masterUrl){
        if (teachers == null || dataSourceMap == null || masterIp == null){
            return ;
        }

        for(Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()){
            String ip = entry.getKey();
            DataSource ds = entry.getValue();


            // 跳过主库 否则会主库再次修改 出现锁超时
            if(ip.equals(masterIp)) continue;
            // if(ip.equals(masterUrl)) continue;


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
                System.out.println("[同步修改成功] 数据库 IP：" + ip );

            } catch (SQLException e) {
                // throw new RuntimeException(e);
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }
        }
    }

    public static void TeacherSyncUtilInsert(Teachers teachers, Map<String, DataSource> dataSourceMap, String masterIp, String masterUrl){
        if (teachers == null || dataSourceMap == null || masterIp == null){return;}
        for(Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()){
            String ip = entry.getKey();
            DataSource ds = entry.getValue();
            // System.out.println(ds);

            // if (ds instanceof HikariDataSource && dataSource instanceof HikariDataSource){
            //     HikariDataSource hikariDs1 = (HikariDataSource) ds;
            //     HikariDataSource hikariDs2 = (HikariDataSource) dataSource;
            //
            //     // 比较JDBC URL
            //     String url1 = hikariDs1.getJdbcUrl();
            //     String url2 = hikariDs2.getJdbcUrl();
            //
            //     // 比较用户名
            //     String username1 = hikariDs1.getUsername();
            //     String username2 = hikariDs2.getUsername();
            //
            //     // 比较密码（注意：密码可能被加密或不可见）
            //     String password1 = hikariDs1.getPassword();
            //     String password2 = hikariDs2.getPassword();
            //     if (url1.equals(url2) && username1.equals(username2) && password1.equals(password2)){
            //         System.err.println("已经执行过.............");
            //         continue;
            //     }
            // }

            System.err.println(ip + "    " + ds + "开始执行教师添加");

            // 跳过主库 否则会主库再次修改 出现锁超时
            if(ip.equals(masterIp)) continue;
            // if (ip.equals(masterUrl)) {
            //     System.out.println("一个数据库出现问题 同时进行同步数据库操作");
            //     continue;
            // }


            try(Connection conn = ds.getConnection()){
                PreparedStatement statement = conn.prepareStatement(
                        "insert into teachers values (?, ?, ?, ?, ?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE teacherid = teacherid"
                );
                statement.setString(1, teachers.getTeacherid());
                statement.setString(2, teachers.getName());
                statement.setInt(3, teachers.getSex());
                statement.setDate(4, teachers.getBirthdate());
                statement.setString(5, teachers.getTitle());
                statement.setString(6, teachers.getTelephone());
                statement.setString(7, teachers.getRemarks());

                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    System.err.println("[同步成功] 数据库 IP：" + ip + "datasource:" + ds);
                }else {
                    System.err.println("[忽略重复] 教师ID已存在: " + teachers.getTeacherid());
                }
            } catch (SQLException e) {
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }
        }
    }

    public static void TeacherSyncUtilDelete(String teacherId, Map<String, DataSource> dataSourceMap, String masterIp,String masterUrl){
        if (teacherId == null || dataSourceMap == null || masterIp == null){return;}
        for(Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()){
            String ip = entry.getKey();
            DataSource ds = entry.getValue();

            System.err.println(ip + "数据库即将执行的ip");
            if(ip.equals(masterIp)) continue;
            // if (ip.equals("101.37.38.162")) continue;
            // if(ip.equals(masterUrl)) continue;
            System.err.println(ip + "删除数据库目前执行的ip");

            try(Connection conn = ds.getConnection()){
                // 先检查是否存在
                PreparedStatement checkStmt = conn.prepareStatement(
                        "SELECT 1 FROM teachers WHERE teacherID = ?"
                );
                checkStmt.setString(1, teacherId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()){
                    PreparedStatement statement = conn.prepareStatement(
                            "delete from teachers where teacherID = ?"
                    );
                    statement.setString(1, teacherId);

                    statement.executeUpdate();
                    System.err.println("[同步成功] 数据库 IP：" + ip );
                }else {
                    System.err.println("[同步成功] 数据库 IP：" + ip );
                }

            } catch (SQLException e) {
                System.err.println("[同步失败] 数据库 IP：" + ip + "，错误：" + e.getMessage());
            }
        }
    }

}
