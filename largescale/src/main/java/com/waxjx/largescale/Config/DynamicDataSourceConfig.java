package com.waxjx.largescale.Config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class DynamicDataSourceConfig {

    @Value("${zookeeper.addresses}")
    private String zkAddresses;
    // @Value("${zookeeper.address}")
    // private String zkAddress;

    // 更新 master 需要的参数
    private static final List<String> ZK_SERVERS = Arrays.asList(
            "47.122.84.149",
            "120.26.234.22",
            "101.37.38.162"
    );
    private static final int ZK_PORT = 2181;

    private CuratorFramework connectToAvailableZK() {
        String[] addressList = zkAddresses.split(",");
        for (String addr : addressList) {
            try {
                System.err.println(addr.trim());
                CuratorFramework client = CuratorFrameworkFactory.newClient(
                        addr.trim(),
                        new ExponentialBackoffRetry(1000, 3)
                );
                client.start();
                // if (client.checkExists().forPath("/") != null) {      // 通过根节点来检测
                //     System.out.println("Zookeeper 连接正常，根节点存在");
                // }else {
                //     System.err.println("Zookeeper 连接异常");
                // }
                client.blockUntilConnected(50, java.util.concurrent.TimeUnit.SECONDS); // 等待连接成功
                if (client.getZookeeperClient().isConnected()) {
                    System.out.println("成功连接到 Zookeeper: " + addr);
                    return client;
                }
            } catch (Exception e) {
                System.err.println("无法连接到 Zookeeper: " + addr + "，错误：" + e.getMessage());
            }
        }
        throw new RuntimeException("无法连接到任何 Zookeeper 节点");
    }


    // Leader 检测并更新 Master
    private void electAndUpdateMaster(CuratorFramework client) throws Exception {
        for (String ip : ZK_SERVERS) {
            String mode = getZookeeperMode(ip, ZK_PORT);
            System.err.println(mode);
            if ("leader".equalsIgnoreCase(mode)) {
                String path = "/config/db/master";
                byte[] data = ip.getBytes();
                if (client.checkExists().forPath(path) != null) {
                    client.setData().forPath(path, data);
                } else {
                    client.create().creatingParentsIfNeeded().forPath(path, data);
                }
                System.out.println("【已查询到新的 Master】: " + ip);
                return;
            }
        }
        System.err.println("【错误】未能找到 Zookeeper Leader 节点！");
    }

    private String getZookeeperMode(String ip, int port) {
        System.out.println(ip);
        System.out.println(port);
        try (Socket socket = new Socket(ip, port);
             OutputStream out = socket.getOutputStream();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 发送 stat 命令
            out.write("stat".getBytes());
            out.flush();
            // 读取响应
            // String response;
            // while ((response = in.readLine()) != null) {
            //     System.err.println(response);
            // }

            //  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //  BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            //
            // // 发送 stat 命令（必须以 \r\n 结束）
            // writer.write("stat\r\n");
            // writer.flush();
            //
            String line;
            // while (reader != null) {
            //     System.err.println(reader.readLine());
            // }
            //

            // 读取响应
            while ((line = in.readLine()) != null) {
                if (line.contains("Mode:")) {
                    return line.split("Mode:")[1].trim();
                }
            }

        } catch (Exception e) {
            System.err.println("无法连接 Zookeeper 或解析响应: " + e.getMessage());
            // System.err.println("无法连接到 Zookeeper 节点: " + ip + ", 错误信息: " + e.getMessage());
        }
        return null;
    }



    @Bean("dataSourceMap")
    public Map<String, DataSource> dataSourceMap() throws Exception {
        // 连接Zookeeper
        // CuratorFramework client = CuratorFrameworkFactory.newClient(
        //         zkAddress,
        //         new ExponentialBackoffRetry(1000, 3)
        // );
        // client.start();
        CuratorFramework client = connectToAvailableZK();



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
            ds.setConnectionTimeout(1500);

            dataSourceMap.put(child, ds);  // key = ip
        }

        return dataSourceMap;
    }

    @Bean("masterIp")
    public String masterIp() throws Exception {
        // CuratorFramework client = CuratorFrameworkFactory.newClient(
        //         zkAddress,
        //         new ExponentialBackoffRetry(1000, 3)
        // );
        // client.start();
        CuratorFramework client = connectToAvailableZK();
        // client.blockUntilConnected(50, java.util.concurrent.TimeUnit.SECONDS); // 等待连接成功
        // System.err.println(client.checkExists().forPath("/"));
        // System.err.println(client.getZookeeperClient().isConnected());


        // 获取 master 先更新下
        electAndUpdateMaster(client);
        return new String(client.getData().forPath("/config/db/master"));
    }

    // @Bean
    // public DataSource dataSource(@Value("#{dataSourceMap[masterIp]}") DataSource masterDataSource) {
    //     return masterDataSource;
    // }
    @Bean
    public DataSource dataSource() throws Exception{

        // 连接Zookeeper
        // CuratorFramework client = CuratorFrameworkFactory.newClient(
        //         zkAddress,
        //         new ExponentialBackoffRetry(1000, 3)
        // );
        // client.start();

        CuratorFramework client = connectToAvailableZK();


        // 每次重新选主
        electAndUpdateMaster(client);

        String masterIp = new String(client.getData().forPath("/config/db/master"));
        // 主库
        String masterUrl = new String(client.getData().forPath("/config/db/" + masterIp + "/url"));
        String masterUser = new String(client.getData().forPath("/config/db/" + masterIp + "/username"));
        String masterPwd = new String(client.getData().forPath("/config/db/" + masterIp + "/password"));
        HikariDataSource masterDs = new HikariDataSource();
        masterDs.setJdbcUrl(masterUrl);
        masterDs.setUsername(masterUser);
        masterDs.setPassword(masterPwd);
        // 设置连接时长
        masterDs.setConnectionTimeout(1500);  // 1.5 秒
        System.err.println(masterDs.getJdbcUrl());
         //从库
        List<String> children = client.getChildren().forPath("/config/db");
        List<HikariDataSource> slaves = children.stream()
                .filter(ip -> !ip.equals("master") && !ip.equals(masterIp) && !ip.equals("url") && !ip.equals("username") && !ip.equals("password"))
                .map(ip -> {
                    try {
                        String url = new String(client.getData().forPath("/config/db/" + ip + "/url"));
                        String user = new String(client.getData().forPath("/config/db/" + ip + "/username"));
                        String pwd = new String(client.getData().forPath("/config/db/" + ip + "/password"));
                        HikariDataSource ds = new HikariDataSource();
                        ds.setJdbcUrl(url);
                        ds.setUsername(user);
                        ds.setPassword(pwd);
                        ds.setConnectionTimeout(1500);  // 1.5 秒
                        System.err.println(ds.getJdbcUrl());
                        return ds;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        return new ReadWriteRoutingDataSource(masterDs, slaves);
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
