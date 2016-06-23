package server.core.db;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/6/13.
 */
public interface ResultSetHandler {

    void handlerResult(ResultSet rs) throws SQLException;

    <T> T getResult();

    ConcurrentHashMap<String, ConcurrentHashMap<String, Method>> setterMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, ConcurrentHashMap<String, Method>> getterMap = new ConcurrentHashMap<>();
}
