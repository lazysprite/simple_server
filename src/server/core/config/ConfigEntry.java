package server.core.config;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/3.
 */
public interface ConfigEntry {

    ConfigEntry getConfigEntry(ConfigKey key);

    boolean isConfigEntry();

    <T extends ConfigBean> T getConfigBean(ConfigKey key);

    void addConfigBean(ConfigBean bean);

    <T extends ConfigBean> Map<ConfigKey, T> getConfigMap();
}
