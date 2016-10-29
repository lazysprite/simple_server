package test;

import server.core.config.ConfigBean;
import server.core.config.ConfigCache;
import server.core.config.ConfigKey;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/4.
 */
public class ConfigTest01 {

    public static void main(String[] args) {
        ArrayList<Test> list = new ArrayList<Test>();
        list.add(new Test(1.0f, 1.0));
        list.add(new Test(1.0f, 1.2));
        list.add(new Test(1.1f, 1.1));
        list.add(new Test(1.2f, 1.2));
        list.add(new Test(1.2f, 1.1));

        ConfigCache.getInstance().load(Test.class, list);


        Test config = ConfigCache.getInstance().getConfigBean(Test.class, 1.1d, 1.2f);
        Test config2 = ConfigCache.getInstance().getConfigBean(Test.class);
    }

    static class Test implements ConfigBean {
        private float index1;
        private double index2;
        public Test(float index1, double index2) {
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
