package server.core.db;

import java.sql.Connection;

/**
 * Created by Administrator on 2016/6/13.
 */
public interface SQLExecutor {
    Connection getConnection();

    void executeSQL(String sql);

    void setHandler(ResultSetHandler handler);
}
