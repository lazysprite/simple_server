package server.test;

import server.core.config.ConfigBean;
import server.core.config.ConfigCache;
import server.core.config.ConfigKey;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Administrator on 2016/6/4.
 */
public class ConfigTest04 {

    public static void main(String[] args) {
        ArrayList<Test> list = new ArrayList<Test>();
        list.add(new Test((byte)1, (short) 1));
        list.add(new Test((byte)1, (short) 2));
        list.add(new Test((byte)1, (short) 3));
        list.add(new Test((byte)1, (short) 4));
        list.add(new Test((byte)1, (short) 5));
        list.add(new Test((byte)1, (short) 6));
        list.add(new Test((byte)1, (short) 7));
        list.add(new Test((byte)1, (short) 8));
        list.add(new Test((byte)1, (short) 9));
        list.add(new Test((byte)1, (short) 10));
        list.add(new Test((byte)2, (short) 1));
        list.add(new Test((byte)2, (short) 2));
        list.add(new Test((byte)2, (short) 3));
        list.add(new Test((byte)2, (short) 4));
        list.add(new Test((byte)2, (short) 5));
        list.add(new Test((byte)2, (short) 6));
        list.add(new Test((byte)2, (short) 7));
        list.add(new Test((byte)2, (short) 8));
        list.add(new Test((byte)2, (short) 9));
        list.add(new Test((byte)2, (short) 10));

        ConfigCache.getInstance().load(Test.class, list);

        Test config = ConfigCache.getInstance().getConfigBean(Test.class, (byte)2, (short)10);
        Map<ConfigKey, Test> map = ConfigCache.getInstance().getConfigMap(Test.class, 2);
        for (Entry<ConfigKey, Test> entry : map.entrySet()) {
            if (entry.getKey().equals(1)) {
                System.out.println("... equals ...");
            }
        }
    }

    static class Test implements ConfigBean {
        private byte index1;
        private short index2;
        public Test(byte index1, short index2) {
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
