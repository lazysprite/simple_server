package server.core.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/6/4.
 */
public class ConfigEntryImp implements ConfigEntry {

    private byte deep;
    private ConcurrentHashMap<ConfigKey, ConfigBean> beanMap;
    private ConcurrentHashMap<ConfigKey, ConfigEntry> entryMap;
    private ConfigType type;
    public ConfigEntryImp(byte deep) {
        this.deep = deep;
        type = ConfigType.EMPTY;
    }

    @Override
    public ConfigEntry getConfigEntry(ConfigKey key) {
        if (type != ConfigType.ENTRY) {
            return null;
        }
        return entryMap.get(key);
    }

    @Override
    public boolean isConfigEntry() {
        return type == ConfigType.ENTRY;
    }

    @Override
    public <T extends ConfigBean> T getConfigBean(ConfigKey key) {
        if (type != ConfigType.MAP) {
            return null;
        }
        return (T) beanMap.get(key);
    }

    @Override
    public void addConfigBean(ConfigBean bean) {
        if (type == ConfigType.EMPTY) {
            type = ConfigType.MAP;
            beanMap = new ConcurrentHashMap<ConfigKey, ConfigBean>();
            beanMap.put(bean.getConfigKey(deep), bean);
        } else if (type == ConfigType.MAP) {
            ConfigKey key = bean.getConfigKey(deep);
            ConfigBean oldBean = beanMap.get(key);
            if (oldBean != null) {
                entryMap = new ConcurrentHashMap<ConfigKey, ConfigEntry>();
                // 需要在深入一层
                ConfigEntry entry;
                for (ConfigBean b : beanMap.values()) {
                    entry = entryMap.get(b.getConfigKey(deep));
                    if (entry == null) {
                        entry = new ConfigEntryImp((byte) (deep + 1));
                        entryMap.put(b.getConfigKey(deep), entry);
                    }
                    if (b.getConfigKey(deep).equals(bean.getConfigKey(deep))) {
                        entry.addConfigBean(bean);
                    }
                    entry.addConfigBean(b);
                }
                beanMap = null;
                type = ConfigType.ENTRY;
            } else {
                beanMap.put(key, bean);
            }
        } else if (type == ConfigType.ENTRY) {
            ConfigEntry entry = entryMap.get(bean.getConfigKey(deep));
            if (entry == null) {
                entry = new ConfigEntryImp((byte) (deep + 1));
                entryMap.put(bean.getConfigKey(deep), entry);
            }
            entry.addConfigBean(bean);
        }
    }

    @Override
    public <T extends ConfigBean> Map<ConfigKey, T> getConfigMap() {
        if (type != ConfigType.MAP) return null;
        return (Map<ConfigKey, T>) beanMap;
    }
}

