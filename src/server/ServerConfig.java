package server;

import com.google.common.base.Preconditions;
import server.core.log.LogUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2016/5/21.
 */
public class ServerConfig {

    private ServerConfig() {
    }

    public final static ConcurrentMap<String, String> properties = new ConcurrentHashMap<>();

    public static void init() {
        try {
            Properties config = new Properties();
            config.load(new FileInputStream("./config/server.properties"));
            for (String key : config.stringPropertyNames()) {
                properties.putIfAbsent(key, config.getProperty(key));
            }
        } catch (IOException e) {
            LogUtil.error("loading config error.", e);
        }
    }

    /**
     * 线程执行器的大小，默认是cpu的 核数 * 2
     */
    public static int EXECUTORSIZE = Runtime.getRuntime().availableProcessors() * 2;

    public static class CodecConfig {
        /**
         * 协议头部
         */
        public static byte HEAD = -81;
        /**
         * 协议尾部
         */
        public static byte TAIL = -82;

    }

    public static int HOTSWAP_INTERVAL = 5; // 热更新间隔时间

    /** 配置端口 */
    public static int getServerPort() {
        return Integer.valueOf(Preconditions.checkNotNull(properties.get("server.port")));
    }

}
