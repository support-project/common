package org.support.project.ormapping.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.support.project.ormapping.exception.ORMappingException;

/**
 * SQLのリソースを読み込む
 */
public class SQLManager {

	/**
	 * シングルトンで管理されたconnectionManagerのインスタンス
	 */
	private static SQLManager sqlManager = null;

	/** SQLを保持するマップ */
	private Map<String, String[]> sqlMap = null;

	/**
	 * コンストラクタ
	 * 
	 * @throws Exception
	 */
	private SQLManager() {
		sqlMap = new HashMap<String, String[]>();
	}

	/**
	 * インスタンスの取得
	 * 
	 * @return ConnectionManagerのインスタンス
	 * @throws Exception
	 */
	public static SQLManager getInstance() {
		if (sqlManager == null) {
			sqlManager = new SQLManager();
		}
		return sqlManager;
	}

	public String getSql(String sqlFilePath) {
		String[] sqls = getSqls(sqlFilePath);
		if (sqls.length > 0) {
			return sqls[0];
		}
		return null;
	}

	/**
	 * キーでSQLを取得
	 * 
	 * @throws IOException
	 */
	public String[] getSqls(String sqlFilePath) {
		try {
			if (sqlMap.containsKey(sqlFilePath)) {
				return sqlMap.get(sqlFilePath);
			}
			BufferedReader bufferedReader = null;

			try {
				bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(sqlFilePath), "UTF-8"));

				List<String> sqls = new ArrayList<String>();
				StringBuffer buffer = new StringBuffer();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					if (!line.startsWith("--")) {
						if (line.indexOf(";") != -1) {
							String[] sp = line.split(";");
							for (String string : sp) {
								if (string != null && sp.length > 0) {
									buffer.append(string);
									sqls.add(buffer.toString());
									buffer.delete(0, buffer.length());
								}
							}
						} else {
							buffer.append(line);
						}
						buffer.append(" ");
					}
				}
				if (buffer.length() > 0) {
					sqls.add(buffer.toString());
				}

				String[] arry = (String[]) sqls.toArray(new String[0]);
				sqlMap.put(sqlFilePath, arry);
				return arry;
			} finally {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}
		} catch (IOException e) {
			throw new ORMappingException(e);
		}
	}

}
