package server.core.executor;

import server.ServerConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/5/9.
 */
public class LogicExecutor {

    private static LogicExecutor instance;
    private LogicExecutor() {
        for (int i = 0; i < executorList.length; i++) {
            executorList[i] = Executors.newSingleThreadExecutor();
        }
    }

    public static LogicExecutor getInstance() {
        if (instance == null) {
            synchronized(LogicExecutor.class) {
                if (instance == null) {
                    instance = new LogicExecutor();
                }
            }
        }
        return instance;
    }

    /**
     * 线程执行器列表
     */
    private ExecutorService[] executorList = new ExecutorService[ServerConfig.EXECUTORSIZE];

    public void execute(Task unit) {
        long sessionId = unit.getChannelSession().getSessionId();
        int index = (int) (sessionId % executorList.length);
        /**
         * 将客户端的请求交给指定的线程处理
         * 这样保证了这个客户端的连接所有的逻辑处理只会由
         * 单个线程处理而且自然地保证了客户端的请求是有序相应的
         */
        executorList[index].execute(unit);
    }
}
