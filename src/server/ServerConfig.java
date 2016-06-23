package server;

/**
 * Created by Administrator on 2016/5/21.
 */
public class ServerConfig {
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
}
