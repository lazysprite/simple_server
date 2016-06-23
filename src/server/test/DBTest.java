package server.test;

import server.core.db.ConnectionPool;
import server.core.db.DBBean;
import server.core.db.DBFacade;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/13.
 */
public class DBTest implements DBBean {

    private int aC;

    private int b;

    private String str;

    private Date time;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getaC() {
        return aC;
    }

    public void setaC(int aC) {
        this.aC = aC;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    @Override
    public String getTableName() {
        return "test01";
    }


    public static void main(String[] args) {
        ConnectionPool.getInstance().init("config/druid.properties");
        DBTest bean = DBFacade.queryBean(DBTest.class, "select * from test01");
        DBTest bean2 = DBFacade.queryBean(DBTest.class, "select * from test01 where a_c = 2");
        Map<Integer, DBTest> hashMap = DBFacade.queryHashMap(DBTest.class, "b", "select * from test01 where a_c = 1");
        Map<Integer, DBTest> concurrentHashMap = DBFacade.queryConcurrentHashMap(DBTest.class, "b", "select * from test01 where a_c = 1");
        List<DBTest> arrayList = DBFacade.queryArrayList(DBTest.class, "select * from test01 where a_c = 1");

    }
}
