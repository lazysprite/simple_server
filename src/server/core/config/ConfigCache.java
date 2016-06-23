package server.core.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ConfigCache {

    private static ConfigCache instance;
    private ConfigCache() {}
    public static ConfigCache getInstance() {
        if (instance == null) {
            synchronized (ConfigCache.class) {
                if (instance == null) {
                    instance = new ConfigCache();
                }
            }
        }
        return instance;
    }

    private ConcurrentHashMap<Class<? extends ConfigBean>, ConfigEntry> cache = new ConcurrentHashMap<Class<? extends ConfigBean>, ConfigEntry>();

    public <T extends ConfigBean> void load(Class<T> clzz, List<T> list) {
        ConfigEntry entry = new ConfigEntryImp((byte) 1);
        for (ConfigBean bean : list) {
            entry.addConfigBean(bean);
        }
        cache.put(clzz, entry);
    }

    public <T extends ConfigBean> T getConfigBean(Class<T> clzz, Object ...args) {
        if (clzz == null) return null;
        ConfigEntry entry = cache.get(clzz);
        for (int i = 0; i < args.length; i++) {
            if (entry == null) return null;
            if (i < args.length - 1) {
                entry = entry.getConfigEntry(new ConfigKey(args[i]));
            } else if (i == args.length - 1) {
                if (entry.isConfigEntry()) {
                    throw new ConfigParameterError("index: " + (i + 1));
                }
                return entry.getConfigBean(new ConfigKey(args[i]));
            }
        }
        return null;
    }

    public <T extends ConfigBean> Map<ConfigKey, T> getConfigMap(Class<T> clzz, Object ...args) {
        if (clzz == null) return null;
        ConfigEntry entry = cache.get(clzz);
        for (int i = 0; i < args.length; i++) {
            if (entry == null) {
                return null;
            }
            if (i < args.length - 1) {
                entry = entry.getConfigEntry(new ConfigKey(args[i]));
            } else if (i == args.length - 1) {
                entry = entry.getConfigEntry(new ConfigKey(args[i]));
                if (entry.isConfigEntry()) {
                    throw new ConfigParameterError("index: " + (i + 1));
                }
                return entry.getConfigMap();
            }
        }
        return null;
    }
}
