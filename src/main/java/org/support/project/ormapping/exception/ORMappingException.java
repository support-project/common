package org.support.project.ormapping.exception;

import org.support.project.common.config.Resources;
import org.support.project.common.exception.SystemException;
import org.support.project.common.util.StringUtils;
import org.support.project.ormapping.config.ORMappingParameter;

public class ORMappingException extends SystemException {
	
	private String sql = null;
	private Object[] params = null;
	
	
	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * メッセージ取得のリソースファイル
	 * @return
	 */
	protected Resources getResources() {
		return Resources.getInstance(ORMappingParameter.OR_MAPPING_RESOURCE);
	}

	public ORMappingException(String key, String... params) {
		super(key, params);
	}

	public ORMappingException(String key, Throwable cause, String... params) {
		super(key, cause, params);
	}

	public ORMappingException(String key, Throwable cause) {
		super(key, cause);
	}

	public ORMappingException(String key) {
		super(key);
	}

	public ORMappingException(Throwable cause) {
		super(cause);
	}

	
	
	
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}
	
	private String sqlDebug(String append) {
		StringBuilder builder = new StringBuilder();
		builder.append(append);
		builder.append("\n----- SQL Infomation -----\n");
		if (StringUtils.isNotEmpty(sql)) {
			builder.append("[sql]").append(sql).append("\n");
		}
		if (params != null) {
			builder.append("[params]").append("\n");
			for (int i = 0; i < params.length; i++) {
				Object object = params[i];
				builder.append("\t[params][").append(i).append("]").append(String.valueOf(object));
				builder.append("\n");
			}
		}
		if (StringUtils.isNotEmpty(sql)) {
			String execute = sql;
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					try {
						Object object = params[i];
						execute = execute.replaceFirst("\\?", String.valueOf(object));
					} catch (Exception e) {
					}
				}
			}
			builder.append("[execute]").append(execute).append("\n");
		}
		
		return builder.toString();
	}
	
	@Override
	public String getMessage() {
		return sqlDebug(super.getMessage());
	}

	@Override
	public String getLocalizedMessage() {
		return sqlDebug(super.getLocalizedMessage());
	}

}
