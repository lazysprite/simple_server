package test;

import server.core.config.ConfigBean;
import server.core.config.ConfigCache;
import server.core.config.ConfigKey;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/4.
 */
public class ConfigTest03 {

    public static void main(String[] args) {
        ArrayList<Test> list = new ArrayList<Test>();
        list.add(new Test("hello1", (short)2));
        list.add(new Test("hello2", (short)1));

        ConfigCache.getInstance().load(Test.class, list);


        Test config = ConfigCache.getInstance().getConfigBean(Test.class, "hello1");
        Test config2 = ConfigCache.getInstance().getConfigBean(Test.class);
    }

    static class Test implements ConfigBean {
        private String index1;
        private short index2;
        public Test(String index1, short index2) {
            this.index1 = index1;
            this.index2 = index2;
        }

        @Override
        public ConfigKey getConfigKey(byte deep) {
            if (deep == 1) return new ConfigKey(index1);
            if (deep == 2) return new ConfigKey(index2);
            return null;
        }
    }


}
