package server.core.actor.service;

import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import server.core.actor.Actor;

import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ServiceInvocationHandle implements InvocationHandler {

    private ServiceActor actor;
    private Class<? extends Service> serviceClass;

    public ServiceInvocationHandle(ServiceActor actor) {
        this.actor = actor;
        for (Class<?> c : actor.getClass().getInterfaces()) {
            if (Service.class.isAssignableFrom(c)) {
                serviceClass = (Class<? extends Service>)c;
                break;
            }
        }
        if (serviceClass == null) {
            throw new IllegalArgumentException("service actor must implements Service interface.");
        }
        for (Method method : serviceClass.getDeclaredMethods()) {
            if (method.getDeclaredAnnotation(AsyncInvocation.class) != null) {
                /** 异步方法必须返回void */
                if (method.getReturnType() != Void.TYPE) {
                    throw new WrongMethodTypeException("asynchronized method return void");
                }
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() != serviceClass) {
            /** 不是service接口下的方法直接调用 */
            return method.invoke(actor, args);
        }
        /** service 接口中的方法，采用actor的方式调用 */
        DelayCallable callable = new DelayCallable();
        callable.setMethod(method)
                .setArgs(args)
                .setInvoker(actor);
        DelayMessage message = new DelayMessage(callable);
        actor.send(message, Actor.free);

        if (method.getDeclaredAnnotation(AsyncInvocation.class) == null) {
            /** 同步调用 */
            return message.get(1500, TimeUnit.MILLISECONDS);
        }
        return null;
    }
}
