package org.support.project.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListUtils {
	
	/**
	 * リストの型をコンバートする
	 * @param list
	 * @param type
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <T> List<T> convertList(final Collection<?> list, final Class<? extends T> type) 
			throws InstantiationException, IllegalAccessException {
		List<T> models = new ArrayList<T>();
		if (list != null) {
			for (Object src : list) {
				if (src != null) {
					T dest = type.newInstance();
					PropertyUtil.copyPropertyValue(src, dest);
					models.add(dest);
				}
			}
		}
		return models;
	}

	public static String toString(Object[] params) {
		if (params == null) {
			return "null";
		}
		StringBuilder builder = new StringBuilder();
		int idx = 0;
		for (Object object : params) {
			if (idx > 0) {
				builder.append("\n");
			}
			builder.append("[").append(idx++).append("]").append(PropertyUtil.reflectionToString(object));
		}
		return builder.toString();
	}
	
	public static String toString(List<?> params) {
		if (params == null) {
			return "null";
		}
		StringBuilder builder = new StringBuilder();
		int idx = 0;
		for (Object object : params) {
			if (idx > 0) {
				builder.append("\n");
			}
			builder.append("[").append(idx++).append("]").append(PropertyUtil.reflectionToString(object));
		}
		return builder.toString();
	}
	
}
