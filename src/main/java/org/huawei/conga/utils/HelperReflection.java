package org.huawei.conga.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelperReflection {

	public static Object getElement(String name, Object object)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = object.getClass().getDeclaredField(name);
		f.setAccessible(true);
		return f.get(object);
	}

	public static Object getElement(String name, Class<? extends Object> classObject, Object object)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = classObject.getDeclaredField(name);
		f.setAccessible(true);
		return f.get(object);
	}

	public static void set(String name, @SuppressWarnings("rawtypes") Class classParent, Object object, Object value)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = classParent.getDeclaredField(name);
		f.setAccessible(true);
		f.set(object, value);
	}

	public static void set(String name, Object object, Object value)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = object.getClass().getDeclaredField(name);
		f.setAccessible(true);
		f.set(object, value);
	}

	public static Object invoke(String name, Object o, Object... parameters) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?>[] types = new Class<?>[parameters.length];
		for (int i = 0; i < types.length; i++) {
			types[i] = parameters[i].getClass();
		}
		Method m = o.getClass().getDeclaredMethod(name, types);
		m.setAccessible(true);
		return m.invoke(o, parameters);
	}

	public static Object invoke(String name, Object o, @SuppressWarnings("rawtypes") Class[] types, Object... parameters)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {
		Method m = o.getClass().getDeclaredMethod(name, types);
		m.setAccessible(true);
		return m.invoke(o, parameters);

	}

}
