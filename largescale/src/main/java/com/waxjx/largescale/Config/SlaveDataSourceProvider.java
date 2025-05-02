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

    @Value("${zookeeper.address}")
    private String zkAddress;

    public List<DataSource> getSlaveDataSources() throws Exception {
        List<DataSource> slaveList = new ArrayList<>();

        CuratorFramework client = CuratorFrameworkFactory.newClient(
                zkAddress,
                new ExponentialBackoffRetry(1000, 3)
        );
        client.start();

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
