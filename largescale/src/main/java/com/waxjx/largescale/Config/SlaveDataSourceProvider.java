package com.waxjx.largescale.Config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


// 从 Zookeeper 获取从库

@Component
public class SlaveDataSourceProvider {

    // @Value("${zookeeper.addresses}")
    // private String zkAddresses;
    @Value("${zookeeper.address}")
    private String zkAddress;


    // private CuratorFramework connectToAvailableZK() {
    //     String[] addressList = zkAddresses.split(",");
    //     for (String addr : addressList) {
    //         try {
    //             CuratorFramework client = CuratorFrameworkFactory.newClient(
    //                     addr.trim(),
    //                     new ExponentialBackoffRetry(1000, 3)
    //             );
    //             client.start();
    //             client.blockUntilConnected(3, java.util.concurrent.TimeUnit.SECONDS); // 等待连接成功
    //             if (client.getZookeeperClient().isConnected()) {
    //                 System.out.println("✅ 成功连接到 Zookeeper: " + addr);
    //                 return client;
    //             }
    //         } catch (Exception e) {
    //             System.err.println("⚠️ 无法连接到 Zookeeper: " + addr + "，错误：" + e.getMessage());
    //         }
    //     }
    //     throw new RuntimeException("❌ 无法连接到任何 Zookeeper 节点");
    // }


    public List<DataSource> getSlaveDataSources() throws Exception {
        List<DataSource> slaveList = new ArrayList<>();

        CuratorFramework client = CuratorFrameworkFactory.newClient(
                zkAddress,
                new ExponentialBackoffRetry(1000, 3)
        );
        client.start();

        // CuratorFramework client = connectToAvailableZK();

        String masterIp = new String(client.getData().forPath("/config/db/master"));
        List<String> children = client.getChildren().forPath("/config/db");

        for (String ip : children) {
            if (ip.equals("master") || ip.equals(masterIp)) continue;

            String base = "/config/db/" + ip;
            String url = new String(client.getData().forPath(base + "/url"));
            String username = new String(client.getData().forPath(base + "/username"));
            String password = new String(client.getData().forPath(base + "/password"));

            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            slaveList.add(ds);
        }

        client.close();
        return slaveList;
    }
}
