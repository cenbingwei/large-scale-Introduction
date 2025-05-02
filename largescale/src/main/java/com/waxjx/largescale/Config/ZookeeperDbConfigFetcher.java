// package com.waxjx.largescale.Config;
//
// import org.apache.curator.framework.CuratorFramework;
// import org.apache.curator.framework.CuratorFrameworkFactory;
// import org.apache.curator.retry.ExponentialBackoffRetry;
//
// public class ZookeeperDbConfigFetcher {
//
//     private final CuratorFramework client;
//
//     public ZookeeperDbConfigFetcher(String zkClusterAddress) {
//         this.client = CuratorFrameworkFactory.newClient(
//                 zkClusterAddress,
//                 new ExponentialBackoffRetry(1000, 3)
//         );
//         this.client.start();
//     }
//
//     public DbInfo getMasterDbInfo() throws Exception {
//         // 1. 获取当前 master 的 IP
//         String masterIp = new String(client.getData().forPath("/config/db/master"));
//
//         // 2. 根据 IP 构建路径，获取对应数据库配置
//         String basePath = "/config/db/" + masterIp;
//         String url = new String(client.getData().forPath(basePath + "/url"));
//         String username = new String(client.getData().forPath(basePath + "/username"));
//         String password = new String(client.getData().forPath(basePath + "/password"));
//
//         return new DbInfo(url, username, password);
//     }
//
//     public static class DbInfo {
//         public final String url, username, password;
//
//         public DbInfo(String url, String username, String password) {
//             this.url = url;
//             this.username = username;
//             this.password = password;
//         }
//     }
// }
