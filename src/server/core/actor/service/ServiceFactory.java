package server.core.actor.service;

import com.google.common.base.Preconditions;

import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ServiceFactory {

    private static ConcurrentMap<String, Service> services = new ConcurrentHashMap<>();

    public static Service newService(ServiceActor actor) {
        Service proxy = (Service) Proxy.newProxyInstance(actor.getClass().getClassLoader(),
                actor.getClass().getInterfaces(),
                new ServiceInvocationHandle(actor));
        return proxy;
    }


    public static void register(String name, Service service) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(service);
        if (services.containsKey(name)) {
            throw new IllegalArgumentException("service already register");
        }
        services.putIfAbsent(name, service);
    }

    public static <T extends Service> T get(String name) {
        Objects.requireNonNull(name);
        return Preconditions.checkNotNull((T) services.get(name));
    }
}
