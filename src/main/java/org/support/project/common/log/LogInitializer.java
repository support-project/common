package org.support.project.common.log;

public interface LogInitializer {
	
	/**
	 * ログを取得
	 * @param type
	 * @return
	 */
	Log createLog(Class<?> type);
	
	
}
