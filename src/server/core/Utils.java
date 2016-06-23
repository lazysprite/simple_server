package server.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/14.
 */
public class Utils {

    /**
     * 解析class的声明的变量，将变量名转化为小写
     * @param clzz
     * @return
     */
    public static Map<String, Field> getStringToFieldMap(Class<?> clzz) {
        Map<String, Field> map = new HashMap<String, Field>();
        Field[] fields = clzz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            map.put(fieldName.toLowerCase(), field);
        }
        return map;
    }

    /**
     * 解析class的声明方法，将方法名转化为小写
     * @param clzz
     * @return
     */
    public static Map<String, Method> getStringToMethod(Class<?> clzz) {
        Map<String, Method> map = new HashMap<String, Method>();
        Method[] methods = clzz.getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            methodName = methodName.toLowerCase();
            map.put(methodName, method);
        }
        return map;
    }
}
