package org.support.project.common.config;

import org.support.project.common.util.StringUtils;


public class AppConfig {
	public static final String APP_CONFIG = "/appconfig.xml";
	public static AppConfig get() {
		if (appConfig == null) {
			appConfig = ConfigLoader.load(AppConfig.APP_CONFIG, AppConfig.class);
		}
		return appConfig;
	}
	private static AppConfig appConfig = null;

	private String systemName;
	
	private String time_zone;
	
	private String basePath;
	private boolean convBasePath = false;

	private String databasePath;
	private boolean convDatabasePath = false;
	
	private String logsPath;
	private boolean convLogsPath = false;
	
	private static boolean disp_env_info = false;
	
	/** ユーザのホームディレクトリ(BasePath)を指定する環境変数のキー */
	private static String envKey = "";
	public static void initEnvKey(String envKey) {
		AppConfig.envKey = envKey;
	}
	
	/**
	 * パスの中に含まれる予約語を置換
	 * @param path
	 * @return
	 */
	public String convPath(String path) {
		if (path.indexOf("{user.home}") != -1) {
			String userHome = System.getProperty("user.home");
			if (userHome.endsWith("/")) {
				userHome = userHome.substring(0, userHome.length() -1);
			}
			path = path.replace("{user.home}", userHome);
		}
		if (path.indexOf("{base.path}") != -1) {
			path = path.replace("{base.path}", getBasePath());
		}
		if (path.indexOf("\\") != -1) {
			path = path.replaceAll("\\\\", "/");
		}
		return path;
	}

	
	/**
	 * @return the time_zone
	 */
	public String getTime_zone() {
		return time_zone;
	}

	/**
	 * @param time_zone the time_zone to set
	 */
	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}
	
	
	public String getDatabasePath() {
		if (StringUtils.isEmpty(databasePath)) {
			return "";
		}
		if (!convDatabasePath) {
			String path = databasePath;
			this.databasePath = convPath(path);
			this.convDatabasePath = true;
		}
		return databasePath;
	}

	public void setDatabasePath(String databasePath) {
		this.databasePath = databasePath;
	}

	/**
	 * @return the basePath
	 */
	public String getBasePath() {
		if (StringUtils.isEmpty(basePath)) {
			return "";
		}
		if (!convBasePath) {
			String path = basePath;
			if (StringUtils.isNotEmpty(envKey)) {
				String envValue = System.getenv(envKey);
				if (StringUtils.isNotEmpty(envValue)) {
					path = envValue;
					if (!disp_env_info) {
						System.out.println("Env [" + envKey + "] was loaded. value is [" + envValue + "].");
						disp_env_info = true;
					}
				}
			}
			this.basePath = convPath(path);
			this.convBasePath = true;
		}
		return basePath;
	}

	/**
	 * @param basePath the basePath to set
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	/**
	 * @return the systemName
	 */
	public String getSystemName() {
		return systemName;
	}

	/**
	 * @param systemName the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	/**
	 * @return the logsPath
	 */
	public String getLogsPath() {
		if (StringUtils.isEmpty(logsPath)) {
			return "";
		}
		if (!convLogsPath) {
			String path = logsPath;
			this.logsPath = convPath(path);
			this.convLogsPath = true;
		}
		return logsPath;
	}

	/**
	 * @param logsPath the logsPath to set
	 */
	public void setLogsPath(String logsPath) {
		this.logsPath = logsPath;
	}

}
