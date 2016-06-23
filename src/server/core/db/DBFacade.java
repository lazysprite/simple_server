package server.core.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/6/11.
 */
public class DBFacade {

    private static <T> T executeSQL(Class<? extends DBBean> clzz, String sql, ResultSetHandler handler, SQLExecutor executor) {
        executor.setHandler(handler);
        executor.executeSQL(sql);
        return handler.getResult();
    }

    private static <T> T query(Class<? extends DBBean> clzz, String sql, ResultSetHandler handler) {
        return executeSQL(clzz, sql, handler, new Query());
    }

    public static <T extends DBBean> T queryBean(Class<T> clzz, String sql) {
        return query(clzz, sql, new BeanResultSetHandler(clzz));
    }

    public static <K, V extends DBBean> HashMap<K, V> queryHashMap(Class<V> clzz, String key, String sql) {
        return query(clzz, sql, new MapResultSetHandler(clzz, key, new HashMap<K, V>()));
    }

    public static <K, V extends DBBean> ConcurrentHashMap<K, V> queryConcurrentHashMap(Class<V> clzz, String key, String sql) {
        return query(clzz, sql, new MapResultSetHandler(clzz, key, new ConcurrentHashMap<K, V>()));
    }

    public static <T extends DBBean> ArrayList<T> queryArrayList(Class<T> clzz, String sql) {
        return query(clzz, sql, new ListResultSetHandler(clzz, new ArrayList<T>()));
    }
}
