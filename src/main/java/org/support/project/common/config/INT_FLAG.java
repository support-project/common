package org.support.project.common.config;


public enum INT_FLAG {
	OFF,
	ON;
	
	public int getValue() {
		return ordinal();
	}
	
	public static INT_FLAG getType(int type) {
		INT_FLAG[] values = values();
		return values[type];
	}

	/**
	 * Integer型のフラグをチェック
	 * @param check
	 * @return
	 */
	public static boolean flagCheck(Integer check) {
		if (check == null) {
			return false;
		}
		if (check.intValue() == INT_FLAG.ON.getValue()) {
			return true;
		}
		return false;
	}

}
