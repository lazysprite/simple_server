package server.core.hotswap;

import server.HotSwapInitilizer;
import server.ServerConfig;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/6/5.
 */
public class HotSwapMonitor {
    private File directory = null;
    private HashMap<String, Long> lastModifyMap = new HashMap<String, Long>();
    public HotSwapMonitor() {}

    public void init() {
        HotSwapInitilizer.init();
        makeDirectory();
        startMonitor();
    }

    private void startMonitor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    searchDirectory();
                    try {
                        TimeUnit.MINUTES.sleep(ServerConfig.HOTSWAP_INTERVAL);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();
    }

    private void searchDirectory() {
        File[] list = directory.listFiles();
        for (File f : list) {
            Long lastModify = lastModifyMap.get(f.getName());
            if (lastModify == null) {
                lastModifyMap.put(f.getName(), f.lastModified());
                hotSwap(f.getAbsolutePath());
            } else if (lastModify > f.lastModified()) {
                hotSwap(f.getAbsolutePath());
            }
        }
    }

    private void hotSwap(String path) {
        try {
            byte[] bytes = SourceLoader.loadSourceFile(path);
            if (bytes == null) return;
            Class<? extends HotSwapInterface> clzz = (Class<? extends HotSwapInterface>) new HotSwapLoader().defineClass(bytes);
            HotSwapInterface instance = clzz.newInstance();
            HotSwapProxy.hotSwap(instance.getHotSwapName(), instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeDirectory() {
        directory = new File("class");
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
            directory.delete();
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        directory.mkdir();
    }

}
