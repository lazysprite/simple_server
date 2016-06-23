package server.core.hotswap;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/6/4.
 */
public interface HotSwapInterface {
    String getHotSwapName();
    ConcurrentHashMap<Class<? extends HotSwapInterface>, HotSwapInterface> map = new ConcurrentHashMap<Class<? extends HotSwapInterface>, HotSwapInterface>();
}
