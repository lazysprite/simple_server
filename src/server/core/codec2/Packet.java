package server.core.codec2;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.*;
import java.util.*;

/**
 * Created by Administrator on 2016/5/23.
 */
public abstract class Packet implements Protocol {

    @Override
    public abstract Protocol executePacket();

    @Override
    public void readFromBuff(ByteBuf buf) {
        List<Field> list = getFields();
        for (Field field : list) {
            field.setAccessible(true);
            Method setter = getSetter(field);
            Object value = readFieldFromBuff(buf, field.getType(), field);
            try {
                setter.invoke(this, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        readFromBuffExtend(buf);
    }

    protected void readFromBuffExtend(ByteBuf buf) {

    }

    private Object readFieldFromBuff(ByteBuf msg, Class<?> clzz, Field field) {
        if (isPrimitive(clzz)) {
            return readPrimitiveFromBuff(msg, clzz);
        } else if (String.class.isAssignableFrom(clzz)) {
            return readStringFromBuff(msg);
        } else if (Protocol.class.isAssignableFrom(clzz)) {
            try {
                Protocol protocol = (Protocol) clzz.newInstance();
                protocol.readFromBuff(msg);
                return protocol;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (clzz.isArray()) {
            int length = msg.readInt();
            Object array = Array.newInstance(clzz.getComponentType(), length);
            for (int i = 0; i < length; i++) {
                Array.set(array, i, readFieldFromBuff(msg, clzz.getComponentType(), null));
            }
            return array;
        } else if (Collection.class.isAssignableFrom(clzz)) {
            int length = msg.readInt();
            Collection collection = null;
            try {
                if (clzz.isInterface()) {
                    if (Set.class.isAssignableFrom(clzz)) {
                        collection = new HashSet<>();
                    } else {
                        collection = new ArrayList<>();
                    }
                } else {
                    collection = (Collection) clzz.newInstance();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < length; i++) {
                ParameterizedType type = (ParameterizedType) field.getGenericType();
                collection.add(readFieldFromBuff(msg, (Class<?>)type.getActualTypeArguments()[0], null));
            }
            return collection;
        } else {
            // TODO 报错
            return null;
        }
        return null;
    }

    private Object readStringFromBuff(ByteBuf msg) {
        // TODO
        int length = msg.readInt();
        byte[] buf = new byte[length];
        msg.readBytes(buf);
        try {
            return new String(buf, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private Object readPrimitiveFromBuff(ByteBuf msg, Class<?> clzz) {
        if (clzz.isAssignableFrom(Byte.class) || clzz.isAssignableFrom(byte.class)) {
            return msg.readByte();
        }
        if (clzz.isAssignableFrom(Short.class) || clzz.isAssignableFrom(short.class)) {
            return msg.readShort();
        }
        if (clzz.isAssignableFrom(Integer.class) || clzz.isAssignableFrom(int.class)) {
            return msg.readInt();
        }
        if (clzz.isAssignableFrom(Long.class) || clzz.isAssignableFrom(long.class)) {
            return msg.readLong();
        }
        if (clzz.isAssignableFrom(Float.class) || clzz.isAssignableFrom(float.class)) {
            return msg.readFloat();
        }
        if (clzz.isAssignableFrom(Double.class) || clzz.isAssignableFrom(double.class)) {
            return msg.readDouble();
        }
        if (clzz.isAssignableFrom(Boolean.class) || clzz.isAssignableFrom(boolean.class)) {
            return msg.readBoolean();
        }
        return 0;
    }

    @Override
    public void writeToBuff(ByteBuf buf) {
        List<Field> list = getFields();
        for (Field field : list) {
            field.setAccessible(true);
            Method getter = getGetter(field);
            Object obj = null;
            try {
                obj = getter.invoke(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (obj != null) {
                writeFieldToBuff(buf, obj);
            } else {
                writeFieldToBuffDefault(buf, field);
            }
        }
        writeToBuffExtend(buf);
    }

    protected void writeToBuffExtend(ByteBuf buf) {

    }

    private void writeFieldToBuffDefault(ByteBuf msg, Field field) {
        if (boolean.class.isAssignableFrom(field.getType()) || Byte.class.isAssignableFrom(field.getType())) {
            msg.writeBoolean(false);
        }
        if (byte.class.isAssignableFrom(field.getType()) || Byte.class.isAssignableFrom(field.getType())) {
            msg.writeByte(0);
        }
        if (short.class.isAssignableFrom(field.getType()) || Short.class.isAssignableFrom(field.getType())) {
            msg.writeShort(0);
        }
        if (int.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType())) {
            msg.writeInt(0);
        }
        if (long.class.isAssignableFrom(field.getType()) || Long.class.isAssignableFrom(field.getType())) {
            msg.writeLong(0);
        }
        if (float.class.isAssignableFrom(field.getType()) || Float.class.isAssignableFrom(field.getType())) {
            msg.writeFloat(0.0f);
        }
        if (double.class.isAssignableFrom(field.getType()) || Double.class.isAssignableFrom(field.getType())) {
            msg.writeDouble(0.0);
        }
        if (String.class.isAssignableFrom(field.getType())) {
            msg.writeInt(0);
        }
        if (field.getType().isArray()) {
            msg.writeInt(0);
        }
        if (Collection.class.isAssignableFrom(field.getType())) {
            msg.writeInt(0);
        }
        if (Protocol.class.isAssignableFrom(field.getType())) {
            Class<?> clzz = field.getType();
            Field[] fields = clzz.getDeclaredFields();
            for (Field f : fields) {
                writeFieldToBuffDefault(msg, f);
            }
        }
    }

    private void writeFieldToBuff(ByteBuf msg, Object obj) {
        if (isPrimitive(obj.getClass())) {
            writePrimitive(msg, obj);
        } else if (String.class.isInstance(obj)) {
            writeString(msg, (String) obj);
        } else if (Protocol.class.isAssignableFrom(obj.getClass())) {
            Protocol protocol = (Protocol) obj;
            protocol.writeToBuff(msg);
        } else if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            int writerIndex = msg.writerIndex();
            int tmpLength = length;
            msg.writeInt(length);
            for (int i = 0; i < length; i++) {
                Object element = Array.get(obj, i);
                if (element == null) {
                    tmpLength--;
                } else {
                    writeFieldToBuff(msg, element);
                }
            }
            msg.setInt(writerIndex, tmpLength); // 修正一下数组的长度
        } else if (Collection.class.isInstance(obj)) {
            Collection<?> collection = (Collection<?>) obj;
            int length = collection.size();
            int writerIndex = msg.writerIndex();
            msg.writeInt(length);
            for (Object element : collection) {
                if (element != null) {
                    writeFieldToBuff(msg, element);
                } else {
                    length--;
                }
            }
            msg.setInt(writerIndex, length);
        }
    }

    private boolean isPrimitive(Class<?> clzz) {
        if (clzz.isPrimitive()) return true;
        if (clzz.isAssignableFrom(Byte.class )) return true;
        if (clzz.isAssignableFrom(Short.class)) return true;
        if (clzz.isAssignableFrom(Integer.class)) return true;
        if (clzz.isAssignableFrom(Long.class)) return true;
        if (clzz.isAssignableFrom(Float.class)) return true;
        if (clzz.isAssignableFrom(Double.class)) return true;
        if (clzz.isAssignableFrom(Boolean.class)) return true;
        return false;
    }

    private void writeString(ByteBuf msg, String obj) {
        byte[] buf = obj.getBytes();
        msg.writeInt(buf.length);
        if (buf.length != 0) {
            msg.writeBytes(buf);
        }
    }

    private void writePrimitive(ByteBuf msg, Object obj) {
        if (obj instanceof Byte) {
            msg.writeByte((byte) obj);
        }
        if (obj instanceof Short) {
            msg.writeShort((short) obj);
        }
        if (obj instanceof Integer) {
            msg.writeInt((int) obj);
        }
        if (obj instanceof Long) {
            msg.writeLong((long) obj);
        }
        if (obj instanceof Float) {
            msg.writeFloat((float) obj);
        }
        if (obj instanceof Double) {
            msg.writeDouble((double) obj);
        }
        if (obj instanceof Boolean) {
            msg.writeBoolean((boolean) obj);
        }
    }

    /**
     * 获取域的getter方法
     * @param field
     * @return
     */
    private Method getGetter(Field field) {
        Method method = null;
        StringBuilder methodName = new StringBuilder();
        String fieldName = field.getName();
        methodName.append("get").append(Character.toUpperCase(fieldName.charAt(0)));
        methodName.append(fieldName.substring(1, fieldName.length()));
        try {
            method = this.getClass().getDeclaredMethod(methodName.toString());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return method;
    }

    /**
     * 获取与的 setter方法
     * @param field
     * @return
     */
    private Method getSetter(Field field) {
        Method method = null;
        String fieldName = field.getName();
        StringBuilder methodName = new StringBuilder();
        methodName.append("set").append(Character.toUpperCase(fieldName.charAt(0)));
        methodName.append(fieldName.substring(1, fieldName.length()));
        try {
            method = this.getClass().getDeclaredMethod(methodName.toString(), field.getType());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return method;
    }

    /**
     * 获取对象的域集合，按声明顺序
     * @return
     */
    private List<Field> getFields() {
        Class<?> clz = this.getClass();
        Field[] fieldList = clz.getDeclaredFields();
        return Arrays.asList(fieldList);
    }


}
