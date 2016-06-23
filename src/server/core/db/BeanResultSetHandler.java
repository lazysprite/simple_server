package server.core.db;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/13.
 */
public class BeanResultSetHandler implements ResultSetHandler {

    private DBBean result;
    private Class<? extends DBBean> clzz;

    public BeanResultSetHandler(Class<? extends DBBean> clzz) {
        this.clzz = clzz;
    }

    @Override
    public void handlerResult(ResultSet rs) throws SQLException {
        String tableName = DBUtil.getTableName(rs);
        Map<String, Method> setterMap = this.setterMap.get(tableName);
        if (setterMap == null) {
            DBUtil.generateMethodMap(clzz, rs);
            setterMap = this.setterMap.get(tableName);
        }
        if (rs.next()) {
            result = DBUtil.getDBBeanByResultSet(rs, setterMap, clzz);
        }
    }

    @Override
    public <T> T getResult() {
        return (T) result;
    }
}
