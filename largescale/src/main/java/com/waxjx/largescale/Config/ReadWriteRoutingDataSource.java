package com.waxjx.largescale.Config;

import javax.sql.DataSource;
import javax.sql.CommonDataSource;
import javax.sql.PooledConnection;
import javax.sql.XADataSource;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetInternal;
import javax.sql.RowSetListener;
import javax.sql.RowSetMetaData;
import javax.sql.RowSetReader;
import javax.sql.RowSetWriter;
import javax.sql.StatementEventListener;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.AbstractDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadWriteRoutingDataSource extends AbstractDataSource {
    // master 和 slaves 是 HikariDataSource， 用于连接的 把之前的连接的@Bean只在初始化的再此处处理，每次接口的时候都要重新决定到底连接哪个数据库
    private final HikariDataSource master;
    private final List<HikariDataSource> slaves;
    private final AtomicInteger counter = new AtomicInteger(0);
    // 最终连上的数据库URL
    public static String masterUrl ;

    public ReadWriteRoutingDataSource(HikariDataSource master, List<HikariDataSource> slaves) {
        this.master = master;
        this.slaves = slaves;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (ReadWriteType.isRead()) {
            int idx = counter.getAndIncrement() % slaves.size();
            System.out.println("【数据源选择】读操作 - 选择从库" + (idx + 1) + ": " + slaves.get(idx));
            try {
                // while(){
                //
                //     return slaves.get(idx++).getConnection();
                // }

                Connection slavesConn = slaves.get(idx).getConnection();
                return slavesConn;
            }catch (Exception e) {
                System.err.println("当前路由访问的 从数据库连接出错，返回失败");
                try {
                    idx = (idx + 1) % slaves.size();
                    Connection slavesConn = slaves.get(idx).getConnection();
                    return slavesConn;
                }catch (Exception e1){
                    return master.getConnection();
                }
            }

        } else {
            System.out.println("【数据源选择】写操作 - 选择主库: " + master.getJdbcUrl());
            try {
                System.err.println("正在连接主数据库" + master.getJdbcUrl());
                return master.getConnection();
            }catch (Exception e){
                int idx = counter.getAndIncrement() % slaves.size();
                try {
                    System.err.println("正在连接从数据库" + slaves.get(idx).getJdbcUrl());
                    return slaves.get(idx).getConnection();
                }catch (Exception e1){
                    idx = (idx + 1) % slaves.size();
                    System.err.println("正在连接从数据库" + slaves.get(idx).getJdbcUrl());
                    return slaves.get(idx).getConnection();
                }

            }
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        if (ReadWriteType.isRead()) {
            int idx = counter.getAndIncrement() % slaves.size();
            System.out.println("【数据源选择】读操作 - 选择从库" + (idx + 1) + ": " + slaves.get(idx));
            try {
                // while(){
                //
                //     return slaves.get(idx++).getConnection();
                // }

                Connection slavesConn = slaves.get(idx).getConnection();
                return slavesConn;
            }catch (Exception e) {
                System.err.println("当前路由访问的 从数据库连接出错，返回失败");
                try {
                    idx = (idx + 1) % slaves.size();
                    Connection slavesConn = slaves.get(idx).getConnection();
                    return slavesConn;
                }catch (Exception e1){
                    return master.getConnection();
                }
            }

        } else {
            System.out.println("【数据源选择】写操作 - 选择主库: " + master);
            try {
                masterUrl = master.getJdbcUrl();
                return master.getConnection();
            }catch (Exception e){
                int idx = counter.getAndIncrement() % slaves.size();
                try {
                    return slaves.get(idx).getConnection();
                }catch (Exception e1){
                    idx = (idx + 1) % slaves.size();
                    return slaves.get(idx).getConnection();
                }

            }
        }
    }

    // 其余方法直接委托给主库
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return master.unwrap(iface);
    }
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return master.isWrapperFor(iface);
    }
    @Override
    public java.io.PrintWriter getLogWriter() {
        try {
            return master.getLogWriter();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void setLogWriter(java.io.PrintWriter out) throws SQLException {
        master.setLogWriter(out);
    }
    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        master.setLoginTimeout(seconds);
    }
    @Override
    public int getLoginTimeout() throws SQLException {
        return master.getLoginTimeout();
    }
    @Override
    public java.util.logging.Logger getParentLogger() {
        try {
            return master.getParentLogger();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
} 