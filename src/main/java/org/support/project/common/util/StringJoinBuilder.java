package org.support.project.common.util;

import java.util.ArrayList;
import java.util.List;

public class StringJoinBuilder {
	
	private List<Object> params = new ArrayList<>();
	
	public StringJoinBuilder() {
		super();
	}
	
	public StringJoinBuilder(List<Object> params) {
		super();
		this.params = params;
	}
	public StringJoinBuilder(Object[] array) {
		super();
		for (Object o : array) {
			params.add(o);
		}
	}
	
	

	public StringJoinBuilder append(Object obj) {
		params.add(obj);
		return this;
	}
	
	public String join(String delimiter) {
		StringBuilder builder = new StringBuilder();
		int count = 0;
		for (Object object : params) {
			if (count > 0) {
				builder.append(delimiter);
			}
			builder.append(object.toString().trim());
			count++;
		}
		return builder.toString();
	}
	
	
	
	
}
