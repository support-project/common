package org.support.project.common.bat;

public interface ConsoleListener {
	
	/**
	 * バッチでコンソールに出力された際に呼ばれるリスナー
	 * @param message
	 */
	void write(String message);
}
