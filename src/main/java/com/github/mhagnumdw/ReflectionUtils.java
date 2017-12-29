package com.github.mhagnumdw;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtils {

	private ReflectionUtils() {
	}

	public static List<Field> getAllFields(Class clazz) {
		List<Field> allFields = new ArrayList<>();
		allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		Class superClass = clazz.getSuperclass();
		if (superClass != null && !superClass.equals(Object.class)) {
			allFields.addAll(getAllFields(superClass));
		}
		return allFields;
	}

}