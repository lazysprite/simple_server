package server.core.handler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/6/2.
 */
public class IDGenerator {

    private AtomicInteger id = new AtomicInteger(0);

    private IDGenerator() {}

    public final long generateId() {
        long temp = id.incrementAndGet();
        if (temp < 0) {
            reset();
            return generateId();
        }
        return temp;
    }

    private void reset() {
        synchronized (this) {
            if (id.get() < 0) {
                id = new AtomicInteger(0);
            }
        }
    }

    /**
     * sessionid 生成器
     */
    public static IDGenerator SESSION = new IDGenerator();
}
