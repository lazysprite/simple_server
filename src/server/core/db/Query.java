package server.core.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Administrator on 2016/6/13.
 */
public class Query implements SQLExecutor {
    private Connection conn;
    private Statement st;
    private ResultSet rs;
    private ResultSetHandler handler;

    public Query() {
    }

    @Override
    public Connection getConnection() {
        return ConnectionPool.getInstance().getConnection();
    }

    @Override
    public void executeSQL(String sql) {
        try {
            conn = getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (handler != null) {
                handler.handlerResult(rs);
            }
        } catch (Exception e) {
            // TODO: 日志
            e.printStackTrace();
        } finally {
            closeConnection();
            closeStatement();
            closeResultSet();
        }
    }

    private void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }

    private void closeStatement() {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            st = null;
        }
    }

    private void closeResultSet() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }
    }

    @Override
    public void setHandler(ResultSetHandler handler) {
        this.handler = handler;
    }
}
