package org.support.project.ormapping.tool.config;

import java.util.List;

public class ORMappingDatabaseInitializeConfig {
	/** 初期化で実施するSQLのパス */
	private List<String> sqlPaths;

	public List<String> getSqlPaths() {
		return sqlPaths;
	}

	public void setSqlPaths(List<String> sqlPaths) {
		this.sqlPaths = sqlPaths;
	}
	
	
}
