package org.support.project.ormapping.tool.impl;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.ormapping.common.NameConvertor;
import org.support.project.ormapping.entity.ColumnDefinition;
import org.support.project.ormapping.tool.DaoGenConfig;

public class DefaultTableSelectMethodCreator {
	/** ログ */
	private static Log log = LogFactory.getLog(DefaultTableSelectMethodCreator.class);

	private CreatorHelper helper = new CreatorHelper();
	private NameConvertor nameConvertor = new NameConvertor();

	private DaoGenConfig config;
	private DefaultTableSQLCreator sqlCreator;
	
	public void writeSelectMethod(DaoGenConfig config, PrintWriter pw) {
		this.config = config;
		this.sqlCreator = new DefaultTableSQLCreator(config);
		
		writePhysicalSelectAll(pw);
		writePhysicalSelectOnKey(pw);
		
		writeSelectAll(pw);
		writeSelectOnKey(pw);
		
		writeSelectOn(pw);
		writePhysicalSelectOn(pw);
		
	}
	
	/**
	 * 複合キーの場合、１つのキーでリストを取得する
	 * @param pw
	 */
	private void writePhysicalSelectOn(PrintWriter pw) {
		List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
		Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
		
		if (primaryKeys.size() <= 1) {
			//キーが一つ以下であれば、一つのキーで取得するメソッドは必要無し
			return;
		}
		List<String> fileNames = sqlCreator.getPhysicalSelectOnSqlFileNames();
		int idx = 0;
		for (ColumnDefinition primary : primaryKeys) {
			String fileName = fileNames.get(idx++);
			writePhysicalSelectOn(pw, primary, fileName);
		}
		
	}

	private void writePhysicalSelectOn(PrintWriter pw,
			ColumnDefinition primary, String fileName) {
		pw.println("\t/**");
		pw.print("\t * ");
		pw.print(primary.getColumn_name());
		pw.println(" でリストを取得");
		pw.println("\t */");
		
		// メソッド定義
		pw.print("\tpublic List<");
		pw.print(config.getEntityClassName());
		pw.print("> physicalSelectOn");
		pw.print(nameConvertor.colmnNameToFeildName(primary.getColumn_name(), true));
		pw.print("(");
		pw.print(helper.getColumnClass(primary));
		pw.print(" "
				+ nameConvertor.colmnNameToFeildName(primary.getColumn_name()));
		pw.println(") {");
		
		// SQLの取得
		pw.print("\t\tString sql = SQLManager.getInstance().getSql(\"");
		pw.print(config.getSqlPackagePath());
		pw.print("/");
		pw.print(fileName);
		pw.println("\");");

		// SQLの実行
		pw.print("\t\treturn executeQueryList(sql, ");
		pw.print(config.getEntityClassName() + ".class, ");
		pw.print(nameConvertor.colmnNameToFeildName(primary.getColumn_name()));
		pw.println(");");

		pw.println("\t}");
	}

	/**
	 * 複合キーの場合、１つのキーでリストを取得する
	 * @param pw
	 */
	private void writeSelectOn(PrintWriter pw) {
		List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
		Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
		
		if (primaryKeys.size() <= 1) {
			//キーが一つ以下であれば、一つのキーで取得するメソッドは必要無し
			return;
		}
		List<String> fileNames = sqlCreator.getSelectOnSqlFileNames();
		int idx = 0;
		for (ColumnDefinition primary : primaryKeys) {
			String fileName = fileNames.get(idx++);
			writeSelectOn(pw, primary, fileName);
		}
	}
	/**
	 *１つのキーでリストを取得する
	 * @param pw
	 * @param fileName 
	 */
	private void writeSelectOn(PrintWriter pw, ColumnDefinition primary, String fileName) {
		pw.println("\t/**");
		pw.print("\t * ");
		pw.print(primary.getColumn_name());
		pw.println(" でリストを取得");
		pw.println("\t */");
		
		// メソッド定義
		pw.print("\tpublic List<");
		pw.print(config.getEntityClassName());
		pw.print("> selectOn");
		pw.print(nameConvertor.colmnNameToFeildName(primary.getColumn_name(), true));
		pw.print("(");
		pw.print(helper.getColumnClass(primary));
		pw.print(" "
				+ nameConvertor.colmnNameToFeildName(primary.getColumn_name()));
		pw.println(") {");
		
		// SQLの取得
		pw.print("\t\tString sql = SQLManager.getInstance().getSql(\"");
		pw.print(config.getSqlPackagePath());
		pw.print("/");
		pw.print(fileName);
		pw.println("\");");

		// SQLの実行
		pw.print("\t\treturn executeQueryList(sql, ");
		pw.print(config.getEntityClassName() + ".class, ");
		pw.print(nameConvertor.colmnNameToFeildName(primary.getColumn_name()));
		pw.println(");");

		pw.println("\t}");
		
	}

	private void writeSelectOnKey(PrintWriter pw) {
		// コメント
		pw.println("\t/**");
		pw.println("\t * キーで1件取得 ");
		pw.println("\t */");

		// メソッド定義
		pw.print("\tpublic ");
		pw.print(config.getEntityClassName());
		pw.print(" selectOnKey(");
		List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
		Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
		int count = 0;
		for (ColumnDefinition columnDefinition : primaryKeys) {
			if (count > 0) {
				pw.print(", ");
			}
			pw.print(helper.getColumnClass(columnDefinition));
			pw.print(" "
					+ nameConvertor.colmnNameToFeildName(columnDefinition
							.getColumn_name()));
			count++;
		}
		pw.println(") {");

		// SQLの取得
		pw.print("\t\tString sql = SQLManager.getInstance().getSql(\"");
		pw.print(config.getSqlPackagePath());
		pw.print("/");
		pw.print(sqlCreator.getSelectOnKeySqlFileName());
		pw.println("\");");

		// SQLの実行
		pw.print("\t\treturn executeQuerySingle(sql, ");
		pw.print(config.getEntityClassName() + ".class, ");
		count = 0;
		for (ColumnDefinition columnDefinition : primaryKeys) {
			if (count > 0) {
				pw.print(", ");
			}
			pw.print(nameConvertor.colmnNameToFeildName(columnDefinition
					.getColumn_name()));
			count++;
		}
		pw.println(");");

		pw.println("\t}");
	}

	private void writePhysicalSelectOnKey(PrintWriter pw) {
		// コメント
		pw.println("\t/**");
		pw.println("\t * キーで1件取得(削除フラグを無視して取得) ");
		pw.println("\t */");

		// メソッド定義
		pw.print("\tpublic ");
		pw.print(config.getEntityClassName());
		pw.print(" physicalSelectOnKey(");
		List<ColumnDefinition> columnDefinitions = config.getTableDefinition().getColumns();
		Collection<ColumnDefinition> primaryKeys = config.getPrimaryKeys(columnDefinitions);
		int count = 0;
		for (ColumnDefinition columnDefinition : primaryKeys) {
			if (count > 0) {
				pw.print(", ");
			}
			pw.print(helper.getColumnClass(columnDefinition));
			pw.print(" "
					+ nameConvertor.colmnNameToFeildName(columnDefinition
							.getColumn_name()));
			count++;
		}
		pw.println(") {");

		// SQLの取得
		pw.print("\t\tString sql = SQLManager.getInstance().getSql(\"");
		pw.print(config.getSqlPackagePath());
		pw.print("/");
		pw.print(sqlCreator.getPhysicalSelectOnKeySqlFileName());
		pw.println("\");");

		// SQLの実行
		pw.print("\t\treturn executeQuerySingle(sql, ");
		pw.print(config.getEntityClassName() + ".class, ");
		count = 0;
		for (ColumnDefinition columnDefinition : primaryKeys) {
			if (count > 0) {
				pw.print(", ");
			}
			pw.print(nameConvertor.colmnNameToFeildName(columnDefinition
					.getColumn_name()));
			count++;
		}
		pw.println(");");

		pw.println("\t}");
	}

	
	
	private void writeSelectAll(PrintWriter pw) {
		// コメント
		pw.println("\t/**");
		pw.println("\t * 全て取得 ");
		pw.println("\t */");

		// メソッド定義
		pw.print("\tpublic List<");
		pw.print(config.getEntityClassName());
		pw.print("> selectAll() { ");
		pw.println();

		// SQLの取得
		pw.print("\t\tString sql = SQLManager.getInstance().getSql(\"");
		pw.print(config.getSqlPackagePath());
		pw.print("/");
		pw.print(sqlCreator.getSelectAllSqlFileName());
		pw.println("\");");

		// SQLの実行
		pw.print("\t\treturn executeQueryList(sql, ");
		pw.println(config.getEntityClassName() + ".class);");

		pw.println("\t}");
	}



	private void writePhysicalSelectAll(PrintWriter pw) {
		// コメント
		pw.println("\t/**");
		pw.println("\t * 全て取得(削除フラグを無視して取得) ");
		pw.println("\t */");

		// メソッド定義
		pw.print("\tpublic List<");
		pw.print(config.getEntityClassName());
		pw.print("> physicalSelectAll() { ");
		pw.println();

		// SQLの取得
		pw.print("\t\tString sql = SQLManager.getInstance().getSql(\"");
		pw.print(config.getSqlPackagePath());
		pw.print("/");
		pw.print(sqlCreator.getPhysicalSelectAllSqlFileName());
		pw.println("\");");

		// SQLの実行
		pw.print("\t\treturn executeQueryList(sql, ");
		pw.println(config.getEntityClassName() + ".class);");

		pw.println("\t}");
	}


}
