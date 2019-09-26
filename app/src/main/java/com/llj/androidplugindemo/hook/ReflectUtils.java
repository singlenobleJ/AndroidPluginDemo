package com.llj.androidplugindemo.hook;

import java.lang.reflect.Field;

public class ReflectUtils {

    /**
     * 获取Field对应的值
     *
     * @param clazz
     * @param target
     * @param name
     * @return
     * @throws Exception
     */
    public static Object getField(Class clazz, Object target, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    /**
     * 获取Field
     *
     * @param clazz
     * @param name
     * @return
     * @throws Exception
     */
    public static Field getField(Class clazz, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    /**
     * 给Field赋值
     *
     * @param clazz
     * @param target
     * @param name
     * @param value
     * @throws Exception
     */
    public static void setField(Class clazz, Object target, String name, Object value) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}