package server.core.hotswap;

/**
 * Created by Administrator on 2016/6/5.
 */
public class HotSwapLoader extends ClassLoader {

    public HotSwapLoader() {
        // TODO	为什么要使用线程的上下文类加载器？
        super(Thread.currentThread().getContextClassLoader());
    }

    /**
     * 将字节流转换为类
     * 其中只要保证字节流里面的内容是符合.class文件的要求的，就可以转换成对应的类
     * 而与.class文件的位置是无关的
     * 简单的说，这种方式，不需要考虑包的路径，也就说只要能够正确的读取出.class文件内容就行的
     * @param bytes
     * @return
     */
    public Class<?> defineClass(byte[] bytes) {
        return this.defineClass(null, bytes, 0, bytes.length);
    }

}
