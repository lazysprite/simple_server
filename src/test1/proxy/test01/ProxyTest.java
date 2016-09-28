package test1.proxy.test01;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2016/9/7.
 */
public class ProxyTest {

    public static void main(String[] args) {
        ProxyHelloWorld proxy = (ProxyHelloWorld) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{ProxyHelloWorld.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.print("proxy hello world");
                method.invoke(proxy, args);
                return null;
            }
        });
        proxy.helloWorld();
    }
}
