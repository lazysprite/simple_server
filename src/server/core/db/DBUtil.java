package server.core.db;

import server.core.Utils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用默认的访问权限，不希望在包以外使用该工具
 * Created by Administrator on 2016/6/14.
 */
public class DBUtil {

    static void generateMethodMap(Class<?> clzz, ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        String tableName = meta.getTableName(1);
        ConcurrentHashMap<String, Method> setterMap = new ConcurrentHashMap<String, Method>();
        ConcurrentHashMap<String, Method> getterMap = new ConcurrentHashMap<String, Method>();
        Map<String, Method> methodMap = Utils.getStringToMethod(clzz);
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String column = meta.getColumnName(i);
            column = column.replace("_", "");   // 支持数据库字段用下划线来间隔
            column = column.toLowerCase();

            StringBuilder builder = new StringBuilder();
            builder.append("set").append(column);
            String setter = builder.toString();
            builder.delete(0, builder.length());
            builder.append("get").append(column);
            String getter = builder.toString();
            Method method  = null;
            try {
                method = methodMap.get(setter);
                if (method != null) {
                    setterMap.put(column, method);
                } else {
                    // TODO: 日志
                    System.out.println("Do not have setter: " + column);
                }
                method = methodMap.get(getter);
                if (method != null) {
                    getterMap.put(column, method);
                } else {
                    // TODO: 日志
                    System.out.println("Do not have getter: " + column);
                }
            } catch (Exception e) {
                // TODO: 日志
                e.printStackTrace();
            }
        }
        ResultSetHandler.setterMap.putIfAbsent(tableName, setterMap);
        ResultSetHandler.getterMap.putIfAbsent(tableName, getterMap);
    }

    static Map<String, Integer> getColumnToIndex(ResultSet rs) throws SQLException {
        Map<String, Integer> map = new HashMap<String, Integer>();
        ResultSetMetaData meta = rs.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String column = meta.getColumnName(i);
            column = column.replace("_", "");
            column = column.toLowerCase();
            map.put(column, i);
        }
        return map;
    }

    static DBBean getDBBeanByResultSet(ResultSet rs, Map<String, Method> setterMap, Class<? extends DBBean> clzz) {
        DBBean bean = null;
        try {
            bean = clzz.newInstance();
            Map<String, Integer> column2Index = getColumnToIndex(rs);
            for (Entry<String, Method> entry : setterMap.entrySet()) {
                Method setter = entry.getValue();
                setter.invoke(bean, rs.getObject(column2Index.get(entry.getKey())));
            }
        } catch  (Exception e) {
            // TODO: 日志
            e.printStackTrace();
        }
        return bean;
    }

    static String getTableName(ResultSet rs) throws SQLException {
        return rs.getMetaData().getTableName(1);
    }
}
