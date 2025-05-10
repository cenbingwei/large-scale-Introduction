package com.waxjx.largescale.Config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfig {

    @Value("${zookeeper.address}")
    private String zkAddress;

    @Bean("dataSourceMap")
    public Map<String, DataSource> dataSourceMap() throws Exception {
        // 连接Zookeeper
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                zkAddress,
                new ExponentialBackoffRetry(1000, 3)
        );
        client.start();

        List<String> children = client.getChildren().forPath("/config/db");
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        for (String child : children) {
            // 忽略 master 节点
            if ("master".equals(child)) continue;
            if ("url".equals(child)) continue;
            if ("username".equals(child)) continue;
            if ("password".equals(child)) continue;

            String jdbcUrl = new String(client.getData().forPath("/config/db/" + child + "/url"));
            String username = new String(client.getData().forPath("/config/db/" + child + "/username"));
            String password = new String(client.getData().forPath("/config/db/" + child + "/password"));

            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(jdbcUrl);
            ds.setUsername(username);
            ds.setPassword(password);

            dataSourceMap.put(child, ds);  // key = ip
        }

        return dataSourceMap;
    }

    @Bean("masterIp")
    public String masterIp() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                zkAddress,
                new ExponentialBackoffRetry(1000, 3)
        );
        client.start();
        return new String(client.getData().forPath("/config/db/master"));
    }

    // @Bean
    // public DataSource dataSource(@Value("#{dataSourceMap[masterIp]}") DataSource masterDataSource) {
    //     return masterDataSource;
    // }
    @Bean
    public DataSource dataSource() throws Exception{

        // 连接Zookeeper
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                zkAddress,
                new ExponentialBackoffRetry(1000, 3)
        );
        client.start();

        // 获取当前 Master 的数据库ip
        String masterIp = new String(client.getData().forPath("/config/db/master"));

        // 从Zookeeper中获取数据库配置
        String jdbcUrl = new String(client.getData().forPath("/config/db/" + masterIp + "/url"));
        String username = new String(client.getData().forPath("/config/db/" + masterIp + "/username"));
        String password = new String(client.getData().forPath("/config/db/" + masterIp + "/password"));

        // 构造并返回 DataSource
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);

        return ds;
    }



}




















// @Configuration
// public class DynamicDataSourceConfig {
//
//     // 注入zkAddress的地址
//     @Value("${zookeeper.address}")
//     private String zkAddress;
//
//     @Bean
//     public DataSource dataSource() throws Exception{
//
//         // 连接Zookeeper
//         CuratorFramework client = CuratorFrameworkFactory.newClient(
//                 zkAddress,
//                 new ExponentialBackoffRetry(1000, 3)
//         );
//         client.start();
//
//         // 获取当前 Master 的数据库ip
//         String masterIp = new String(client.getData().forPath("/config/db/master"));
//
//         // 从Zookeeper中获取数据库配置
//         String jdbcUrl = new String(client.getData().forPath("/config/db/" + masterIp + "/url"));
//         String username = new String(client.getData().forPath("/config/db/" + masterIp + "/username"));
//         String password = new String(client.getData().forPath("/config/db/" + masterIp + "/password"));
//
//         // 构造并返回 DataSource
//         HikariDataSource ds = new HikariDataSource();
//         ds.setJdbcUrl(jdbcUrl);
//         ds.setUsername(username);
//         ds.setPassword(password);
//
//         return ds;
//     }
//
//
// }
