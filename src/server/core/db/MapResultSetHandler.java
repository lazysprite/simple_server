package server.core.db;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/15.
 */
public class MapResultSetHandler implements ResultSetHandler {

    private Class<? extends DBBean> clzz;
    private String key;
    private Map<Object, DBBean> result;

    @Override
    public void handlerResult(ResultSet rs) throws SQLException {
        String tableName = DBUtil.getTableName(rs);
        Map<String, Method> setterMap = this.setterMap.get(tableName);
        if (setterMap == null) {
            DBUtil.generateMethodMap(clzz, rs);
            setterMap = this.setterMap.get(tableName);
        }
        Method getter = getterMap.get(tableName).get(key);
        if (getter == null) throw new GetterMethodNotFoundException(tableName + "|column:" + key);
        while (rs.next()) {
            DBBean bean = DBUtil.getDBBeanByResultSet(rs, setterMap, clzz);
            Object keyValue = null;
            try {
                keyValue = getter.invoke(bean);
            } catch (Exception e) {
                // TODO: 日志
                e.printStackTrace();
            }
            result.put(keyValue, bean);
        }
    }

    @Override
    public <T> T getResult() {
        return (T) result;
    }

    public <T extends DBBean> MapResultSetHandler(Class<T> clzz, String column, Map<?, T> map) {
        this.clzz = clzz;
        this.result =(Map<Object, DBBean>) map;
        this.key = column;
    }
}
