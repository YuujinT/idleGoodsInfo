package com.example.idlegoodsinfo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import java.util.Objects;

public final class DataSourceManager {
    private static HikariDataSource dataSource;

    private DataSourceManager() {
    }

    public static synchronized void initialize(String jdbcUrl, String username, String password) {
        if (dataSource != null) {
            return;
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Objects.requireNonNull(jdbcUrl, "jdbcUrl must not be null"));
        config.setUsername(Objects.requireNonNull(username, "username must not be null"));
        config.setPassword(Objects.requireNonNull(password, "password must not be null"));
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(10_000);
        config.setIdleTimeout(300_000);
        config.setPoolName("IdleGoodsInfoPool");
        dataSource = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource has not been initialized yet");
        }
        return dataSource;
    }

    public static synchronized void close() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }
}
