package com.waxjx.largescale.Config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DynamicDataSourceConfig {

    @Value("${zookeeper.address}")
    private String zkAddress;

    @Bean
    public DataSource dataSource() throws Exception{
        // 连接Zookeeper
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                zkAddress,
                new ExponentialBackoffRetry(1000, 3)
        );
        client.start();

        // 从Zookeeper中获取数据库配置
        String jdbcUrl = new String(client.getData().forPath("/config/db/url"));
        String username = new String(client.getData().forPath("/config/db/username"));
        String password = new String(client.getData().forPath("/config/db/password"));

        // 构造并返回 DataSource
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);

        return ds;
    }


}
