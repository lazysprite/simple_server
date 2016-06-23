package server.core.db;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/16.
 */
public class ListResultSetHandler implements ResultSetHandler {

    private Class<? extends DBBean> clzz;
    private List<DBBean> result;

    @Override
    public void handlerResult(ResultSet rs) throws SQLException {
        String tableName = DBUtil.getTableName(rs);
        Map<String, Method> setterMap = this.setterMap.get(tableName);
        if (setterMap == null) {
            DBUtil.generateMethodMap(clzz, rs);
            setterMap = this.setterMap.get(tableName);
        }
        while (rs.next()) {
            DBBean bean = DBUtil.getDBBeanByResultSet(rs, setterMap, clzz);
            result.add(bean);
        }
    }

    @Override
    public <T> T getResult() {
        return (T) result;
    }

    public <T extends DBBean> ListResultSetHandler(Class<T> clzz, ArrayList<T> list) {
        this.clzz = clzz;
        result = (List<DBBean>) list;
    }
}
