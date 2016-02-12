package org.support.project.common.util;

import java.util.Comparator;

import org.support.project.common.classanalysis.ClassAnalysis;
import org.support.project.common.classanalysis.ClassAnalysisFactory;
import org.support.project.common.exception.SystemException;

public class PropertyComparator<T> implements Comparator<T> {
	/** 比較するプロパティ */
	private String[] properties;
	/**
	 * コンストラクタ
	 * @param properties 比較するプロパティ
	 */
	public PropertyComparator(String... properties) {
		this.properties = properties;
	}
	
	@Override
	public int compare(T o1, T o2) {
		if (o1 == null && o2 == null) {
			return 0; //同じ
		}
		if (o1 == null && o2 != null) {
			return 1; //o1が前
		}
		if (o1 != null && o2 == null) {
			return -1; //o2が前
		}
		
		ClassAnalysis analysis = ClassAnalysisFactory.getClassAnalysis(o1.getClass());
		for (String property : properties) {
			Object v1 = PropertyUtil.getPropertyValue(o1, property);
			Object v2 = PropertyUtil.getPropertyValue(o2, property);
			
			if (v1 == null && v2 == null) {
				continue;
			}
			if (v1 == null && v2 != null) {
				return 1; //o1が前
			}
			if (v1 != null && v2 == null) {
				return -1; //o2が前
			}
			
			if (!Comparable.class.isAssignableFrom(v1.getClass())) {
				StringBuilder builder = new StringBuilder();
				builder.append("compare error.)");
				builder.append("\n\tClass:").append(v1.getClass());
				builder.append("\n\tProperty:").append(property);
				throw new SystemException(builder.toString());
			}
			
			Comparable c1 = (Comparable) v1;
			Comparable c2 = (Comparable) v2;
			
			int result = c1.compareTo(c2);
			if (result != 0) {
				return result;
			}
		}
		
		return 0;
	}

}
