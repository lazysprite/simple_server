package server.core.config;

import java.util.DoubleSummaryStatistics;

/**
 * 解决配置中的key类型不一导致找不到配置entry和config的情况
 * Created by Administrator on 2016/6/4.
 */
public class ConfigKey {

    private Object key;

    public ConfigKey(Object key) {
        this.key = key;
    }

    @Override
    public int hashCode() {
        if (key instanceof String) {
            return key.hashCode();
        }
        if (key instanceof Byte ||
                key instanceof Short ||
                key instanceof Integer ||
                key instanceof Long) {
            return ((Number) key).intValue();
        }
        if (key instanceof Float ||
                key instanceof Double) {
            return new Float(((Number) key).floatValue()).hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConfigKey) {
            obj = ((ConfigKey) obj).getKey();
        }
        if (key instanceof String) {
            return key.equals(obj);
        }
        if (obj instanceof Double || obj instanceof Float) {
            float temp = ((Number) obj).floatValue();
            return ((Number) key).floatValue() == temp;
        }
        if (obj instanceof Byte ||
                obj instanceof Short ||
                obj instanceof Integer ||
                obj instanceof Long) {
            return ((Number) key).intValue() == ((Number) obj).intValue();
        }
        return super.equals(obj);
    }

    public Object getKey() {
        return key;
    }
}
