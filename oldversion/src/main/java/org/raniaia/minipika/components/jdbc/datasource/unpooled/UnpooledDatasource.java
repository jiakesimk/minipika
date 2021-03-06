package org.jiakesiws.minipika.components.jdbc.datasource.unpooled;

/* ************************************************************************
 *
 * Copyright (C) 2020 2B键盘 All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ************************************************************************/

/*
 * Creates on 2020/3/25.
 */

import lombok.Getter;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Not support connection pool the DataSource.
 *
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
public class UnpooledDatasource implements DataSource {

    // 负责加载驱动的类加载器
    protected ClassLoader driverClassLoader;

    // 保存URL，DRIVER等属性的POJO
    @Getter
    protected Dsi dsi;

    public UnpooledDatasource() {
    }

    public UnpooledDatasource(Dsi dsi) {
        this(dsi,null);
    }

    public UnpooledDatasource(Dsi dsi, ClassLoader classLoader) {
        this.dsi = dsi;
        if(classLoader != null){
            this.driverClassLoader = classLoader;
        }else{
            this.driverClassLoader = dsi.driverClassLoader;
        }
    }

    private Connection doGetConnection() throws SQLException {
        return doGetConnection(dsi.username,dsi.password);
    }

    private Connection doGetConnection(String username,String password) throws SQLException {
        initializeDriver();
        Driver driver = Dsi.registerDrivers.get(dsi.driver);
        Connection connection = driver.connect(dsi.url, Dsi.buildDriverInfo(username,password));
        configureConnection(connection);
        return connection;
    }

    private void configureConnection(Connection conn) throws SQLException {
        // 是否自动提交
        if (dsi.autoCommit != null && dsi.autoCommit != conn.getAutoCommit()) {
            conn.setAutoCommit(dsi.autoCommit);
        }
    }

    /**
     * 初始化当前{@link Dsi#driver}中的驱动
     */
    private synchronized void initializeDriver() throws SQLException {
        if (!Dsi.registerDrivers.containsKey(dsi.driver)) {
            Class<?> driver = null;
            try {
                driver = Class.forName(dsi.driver, true, driverClassLoader);
                Driver driverInstance = (Driver) driver.newInstance();
                //
                // 代理的作用是防止在多线程环境下实例化驱动导致死锁问题
                //
                DriverManager.registerDriver(new DriverProxy(driverInstance));
                Dsi.registerDrivers.put(dsi.driver, driverInstance);
            } catch (Exception e) {
                throw new SQLException("Error setting jdbc driver on UnpooledDataSource. Cause: " + e);
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return doGetConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return doGetConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + " is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    //
    // 驱动代理
    //
    class DriverProxy implements Driver {

        final Driver d;

        DriverProxy(Driver d) {
            this.d = d;
        }

        @Override
        public Connection connect(String url, Properties info) throws SQLException {
            return d.connect(url, info);
        }

        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return d.acceptsURL(url);
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
            return d.getPropertyInfo(url, info);
        }

        @Override
        public int getMajorVersion() {
            return d.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return d.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return d.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return d.getParentLogger();
        }
    }

}
