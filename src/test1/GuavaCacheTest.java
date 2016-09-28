package test1;

import com.google.common.cache.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/7/28.
 */
public class GuavaCacheTest {

    public static void main(String[] args) {
        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .concurrencyLevel(8)
                .expireAfterWrite(8, TimeUnit.SECONDS)
                .initialCapacity(10)
                .maximumSize(100)
                .recordStats()
                .removalListener(new RemovalListener<Integer, String>() {
                    @Override
                    public void onRemoval(RemovalNotification<Integer, String> removalNotification) {
                        System.out.println("removal:" + removalNotification.getKey() + "value:" + removalNotification.getValue());
                    }
                })
                .build(new CacheLoader<Integer, String>() {
                    @Override
                    public String load(Integer integer) throws Exception {
                        System.out.println("loading:" + integer);
                        return "test" + integer;
                    }
                });
        for (int i = 0; i < 20; i++) {
            try {
                System.out.println("read:" + cache.get(1));
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("=========cache status===========");
        System.out.println(cache.stats().toString());
    }

}
