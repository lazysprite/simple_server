package server.core.hotswap;

import java.util.Map.Entry;
/**
 * Created by Administrator on 2016/6/4.
 */
public class HotSwapProxy {

    public static <T extends HotSwapInterface> T getInterface(Class<T> clzz) {
        if (!clzz.isInterface()) {
            throw new HotSwapUseException(clzz.getSimpleName() + " must be interface.");
        }
        HotSwapInterface instance = HotSwapInterface.map.get(clzz);
        if (instance == null) {
            throw new HotSwapInitException(clzz.getSimpleName() + " must be init.");
        }
        return (T) HotSwapInterface.map.get(clzz);
    }

    public static boolean hotSwap(String managerName, HotSwapInterface swap) {
        if (swap == null) return false;
        for (Entry<Class<? extends HotSwapInterface>, HotSwapInterface> entry : HotSwapInterface.map.entrySet()) {
            if (entry.getValue().getHotSwapName().equals(managerName)) {
                HotSwapInterface.map.put(entry.getKey(), swap);
                // TODO 热更日志
                System.out.println("hotswap " + swap.getHotSwapName());
                return true;
            }
        }
        return false;
    }
}
