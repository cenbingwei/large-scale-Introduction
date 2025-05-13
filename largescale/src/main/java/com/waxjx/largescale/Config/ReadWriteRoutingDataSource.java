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
import org.springframework.jdbc.datasource.AbstractDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadWriteRoutingDataSource extends AbstractDataSource {
    private final DataSource master;
    private final List<DataSource> slaves;
    private final AtomicInteger counter = new AtomicInteger(0);

    public ReadWriteRoutingDataSource(DataSource master, List<DataSource> slaves) {
        this.master = master;
        this.slaves = slaves;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (ReadWriteType.isRead()) {
            int idx = counter.getAndIncrement() % slaves.size();
            System.out.println("【数据源选择】读操作 - 选择从库" + (idx + 1) + ": " + slaves.get(idx));
            return slaves.get(idx).getConnection();
        } else {
            System.out.println("【数据源选择】写操作 - 选择主库: " + master);
            return master.getConnection();
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        if (ReadWriteType.isRead()) {
            int idx = counter.getAndIncrement() % slaves.size();
            System.out.println("【数据源选择】读操作 - 选择从库" + (idx + 1) + ": " + slaves.get(idx));
            return slaves.get(idx).getConnection(username, password);
        } else {
            System.out.println("【数据源选择】写操作 - 选择主库: " + master);
            return master.getConnection(username, password);
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