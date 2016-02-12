package org.support.project.common.bat;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.di.Container;

/**
 * Javaのプログラムをバッチで実行(別プロセスでjavaを起動する)
 * 
 * @author koda
 *
 */
public class JavaJob implements Job {
	/** ログ */
	private static final Log LOG = LogFactory.getLog(JavaJob.class);

	public static JavaJob get() {
		return Container.getComp(JavaJob.class);
	}
	
	/** Jarが格納されているディレクトリ */
	private List<File> jarDirs = new ArrayList<>();
	/** クラスパスのディレクトリ */
	private List<File> classPathDirs = new ArrayList<>();
	
	/** 実行するメインクラス */
	private String mainClass;
	/** メイン関数に渡す引数 */
	private List<String> params = new ArrayList<>();
	/** カレントディレクトリ */
	private File currentDirectory = null;

	/** 環境変数 */
	private Map<String, String> environment = new LinkedHashMap<>();
	
	/** javaコマンドで -jar で実行するかどうか */
	private boolean jarOption;

	/** コンソール出力を渡すリスナー */
	private ConsoleListener consoleListener = null;

	/**
	 * 実行するメインクラスを設定します。
	 * 
	 * @param mainClass
	 *            実行するメインクラス
	 */
	public JavaJob setMainClass(String mainClass) {
		this.mainClass = mainClass;
		return this;
	}
	
	/**
	 * クラスパスのディレクトリを追加
	 * 
	 * @param dir
	 * @return
	 */
	public JavaJob addClassPathDir(File dir) {
		this.classPathDirs.add(dir);
		return this;
	}

	/**
	 * クラスパスのディレクトリを追加
	 * 
	 * @param dir
	 * @return
	 */
	public JavaJob addjarDir(File dir) {
		this.jarDirs.add(dir);
		return this;
	}

	/**
	 * 引数追加
	 * 
	 * @param param
	 * @return
	 */
	public JavaJob addParam(String param) {
		this.params.add(param);
		return this;
	}

	/**
	 * 環境変数に値を追加
	 * @param key
	 * @param value
	 */
	public JavaJob addEnvironment(String key, String value) {
		this.environment.put(key, value);
		return this;
	}
	
	
	@Override
	public JobResult execute() throws Exception {
		BatJob batJob = new BatJob();
		if (currentDirectory != null) {
			batJob.setCurrentDirectory(currentDirectory);
		}

		batJob.addCommand("java");
		if (!jarDirs.isEmpty() || !classPathDirs.isEmpty()) {
			batJob.addCommand("-classpath");
			batJob.addCommand(makeClassPath());
		}
		if (jarOption) {
			batJob.addCommand("-jar");
		}
		batJob.addCommand(mainClass);
		
		if (consoleListener != null) {
			batJob.setConsoleListener(consoleListener);
		}
		
		if (!environment.isEmpty()) {
			Iterator<String> iterator = environment.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				batJob.addEnvironment(key, environment.get(key));
			}
		}
		
		return batJob.execute();
	}

	/**
	 * クラスパスの生成
	 * 
	 * @param string
	 * @return
	 */
	private String makeClassPath() {
		StringBuilder builder = new StringBuilder();
		int count = 0;
		for (File dir : jarDirs) {
			if (count > 0) {
				builder.append(File.pathSeparator);
			}
			appendLibPath(builder, dir);
			count++;
		}
		for (File dir : classPathDirs) {
			String path = dir.getPath();
			builder.append(File.pathSeparator);
			builder.append(path);
		}
		return builder.toString();
	}

	/**
	 * 再帰的にクラスパスを取得
	 * 
	 * @param builder
	 * @param dir
	 */
	private void appendLibPath(StringBuilder builder, File dir) {
		LOG.trace(dir);
		String path = dir.getPath() + File.separator + "*";
		builder.append(path);

		File[] files = dir.listFiles();
		if (files != null) {
			for (File child : files) {
				if (child.isDirectory()) {
					appendLibPath(builder, child);
				}
			}
		}
	}

	public void setCurrentDirectory(File currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

	public void setJarOption(boolean jarOption) {
		this.jarOption = jarOption;
	}
	/**
	 * @param consoleListener the consoleListener to set
	 */
	public void setConsoleListener(ConsoleListener consoleListener) {
		this.consoleListener = consoleListener;
	}

}
