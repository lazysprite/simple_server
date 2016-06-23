package server.core.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Administrator on 2016/6/13.
 */
public class ConnectionPool {
    private static ConnectionPool instance = new ConnectionPool();
    private ConnectionPool(){}
    public static ConnectionPool getInstance() {
        return instance;
    }

    private DruidDataSource dataSource;

    public void init(String configPath) {
        Properties properties = getConfigByPath(configPath);
        try {
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: how?
        }

    }

    private Properties getConfigByPath(String configPath) {
        InputStream in = null;
        Properties p = new Properties();
        try {
            in = new FileInputStream(configPath);
            p.load(in);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 看看怎么处理好
            System.exit(0);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
