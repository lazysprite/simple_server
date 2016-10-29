package server.core.actor.service;

import server.core.actor.Actor;
import server.core.log.LogUtil;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2016/10/29.
 */
public class DelayCallable implements Callable {

    private Method method;
    private Actor invoker;
    private Object[] args;

    @Override
    public Object call() throws Exception {
        /** 饶过访问限制，避免有些不是public的interface访问不到 */
        method.setAccessible(true);
        Object result = null;
        try {
            result = method.invoke(invoker, args);
        } catch (Throwable e) {
            /** 这里catch住异常，防止异步执行的时候没有报错提示 */
            LogUtil.error("delay call exception", e);
        } finally {
            method.setAccessible(false);
        }
        return result;
    }

    public DelayCallable setMethod(Method method) {
        this.method = method;
        return this;
    }

    public DelayCallable setInvoker(Actor invoker) {
        this.invoker = invoker;
        return this;
    }

    public DelayCallable setArgs(Object[] args) {
        this.args = args;
        return this;
    }
}
