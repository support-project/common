package org.support.project.di.exception;

import org.support.project.common.config.Resources;
import org.support.project.common.exception.SystemException;
import org.support.project.di.config.DIParameter;


/**
 * DI操作で発生するException
 */
public class DIException extends SystemException {
	/**
	 * シリアルバージョン
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * メッセージ取得のリソースファイル
	 * @return
	 */
	protected Resources getResources() {
		return Resources.getInstance(DIParameter.DI_RESOURCE);
	}

	public DIException(String key, String... params) {
		super(key, params);
	}

	public DIException(String key, Throwable cause, String... params) {
		super(key, cause, params);
	}

	public DIException(String key, Throwable cause) {
		super(key, cause);
	}

	public DIException(String key) {
		super(key);
	}

	public DIException(Throwable cause) {
		super(cause);
	}

}
